package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;

import java.util.List;

public class MergeOperator extends Operator {
    private final InputPortExtender inputPortExtender = new InputPortExtender("Input", getInputPorts());
    private final OutputPort outputPort = getOutputPorts().createPort("Output");
    public MergeOperator(OperatorDescription description) {
        super(description);
        inputPortExtender.ensureMinimumNumberOfPorts(2);
    }

    public Pack merge(Pack pack1,Pack pack2){
        List<AbstractNode> nodes1=pack1.getModel().getLastStep().nodes;
        List<AbstractNode> nodes2=pack2.getModel().getLastStep().nodes;
        int len1=nodes1.size();
        int len2=nodes2.size();
        int less=Math.min(len1,len2);
        int index_diff=0;
        for(int i=0;i<less;i++){
            if(nodes1.get(i)==nodes2.get(i)){
            }else{
                index_diff=i;
                break;
            }
        }
        AbstractNode node= nodes2.get(index_diff);
        int node_idx=node.getIndex();
        AbstractNode previous=nodes2.get(index_diff-1);
        Boolean yes=previous.YesSon.isEmpty();
        node.parents.remove(previous);
        node.addParent(nodes1.get(index_diff),yes);
        pack1.getModel().getLastStep().addNode(node);
        if (!node.YesSon.isEmpty()){
            for (AbstractNode son:node.YesSon){
                change_index(nodes2.get(node_idx),node,son,nodes2,nodes1);
            }
        }
        if (!node.NoSon.isEmpty()){
            for (AbstractNode son:node.NoSon){
                change_index(nodes2.get(node_idx),node,son,nodes2,nodes1);
            }
        }
        return pack1;
    }

    public void change_index(AbstractNode parent_old,AbstractNode parent_new,AbstractNode son, List<AbstractNode> old_nodes,List<AbstractNode> new_nodes){
        AbstractNode old_node=old_nodes.get(son.getIndex());
        if(new_nodes.get(parent_new.getIndex()).YesSon.contains(son)){
            new_nodes.get(parent_new.getIndex()).YesSon.remove(son);
            son.parents.remove(parent_old);
            son.parents.add(parent_new);
            new_nodes.get(parent_new.getIndex()).YesSon.add(son);
            new_nodes.add(son);
        }
        if(new_nodes.get(parent_new.getIndex()).NoSon.contains(son)){
            new_nodes.get(parent_new.getIndex()).NoSon.remove(son);
            son.parents.remove(parent_old);
            son.parents.add(parent_new);
            new_nodes.get(parent_new.getIndex()).NoSon.add(son);
            new_nodes.add(son);
        }
        if (!son.YesSon.isEmpty()){
            for (AbstractNode Yson:son.YesSon){
                change_index(old_node,son,Yson,old_nodes,new_nodes);
            }
        }
        if (!son.NoSon.isEmpty()){
            for (AbstractNode Nson:son.NoSon){
                change_index(old_node,son,Nson,old_nodes,new_nodes);
            }
        }
    }

    @Override
    public void doWork() throws OperatorException {
        List<Pack> packs=inputPortExtender.getData(Pack.class,true);
        Pack pack1=packs.get(0);
        for (Pack pack:packs){
            pack1=merge(pack1,pack);
        }
        outputPort.deliver(pack1);
    }
}
