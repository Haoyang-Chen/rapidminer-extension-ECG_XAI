package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.LogService;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class MergeOperator extends Operator {
    private final InputPortExtender inputPortExtender = new InputPortExtender("Input", getInputPorts());
    private final OutputPort outputPort = getOutputPorts().createPort("Output");
    public MergeOperator(OperatorDescription description) {
        super(description);
        inputPortExtender.ensureMinimumNumberOfPorts(2);
    }

    public Pack mergePack(Pack pack1,Pack pack2){
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

        if (index_diff==0&&len1==len2){
            LogService.getRoot().log(Level.INFO, "BBBB"+ index_diff);
            return pack1;
        }
        LogService.getRoot().log(Level.INFO, "AAAA"+ index_diff);
        AbstractNode diff_node=nodes2.get(index_diff);
        Set<AbstractNode> parents=diff_node.parents;
        nodes1.addAll(nodes2.subList(index_diff,len2));
        for(AbstractNode parent:parents){
            int id=parent.getIndex();
            LogService.getRoot().log(Level.INFO, String.valueOf(id));
            if(nodes2.get(id).YesSon.contains(diff_node)){
                nodes1.get(id).YesSon.add(diff_node);
            }else {
                nodes1.get(id).NoSon.add(diff_node);
            }
        }
        pack1.getModel().getLastStep().flushIndex();
//        AbstractNode node= nodes2.get(index_diff);
//        int node_idx=node.getIndex();
//        AbstractNode previous=nodes2.get(index_diff-1);
//        Boolean yes=previous.YesSon.isEmpty();
//        node.parents.remove(previous);
//        node.addParent(nodes1.get(index_diff),yes);
//        pack1.getModel().getLastStep().addNode(node);
//        if (!node.YesSon.isEmpty()){
//            for (AbstractNode son:node.YesSon){
//                change_index(nodes2.get(node_idx),node,son,nodes2,nodes1);
//            }
//        }
//        if (!node.NoSon.isEmpty()){
//            for (AbstractNode son:node.NoSon){
//                change_index(nodes2.get(node_idx),node,son,nodes2,nodes1);
//            }
//        }
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
//        LogService.getRoot().log(Level.INFO, String.valueOf(packs.size()));
        for (Pack pack:packs){
            pack1=mergePack(pack1,pack);
        }
        LogService.getRoot().log(Level.INFO, "AAAA");
        outputPort.deliver(pack1);
    }
}
