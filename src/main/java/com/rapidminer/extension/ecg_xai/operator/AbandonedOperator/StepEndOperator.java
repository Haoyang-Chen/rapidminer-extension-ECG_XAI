package com.rapidminer.extension.ecg_xai.operator.AbandonedOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.LogService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

public class StepEndOperator extends Operator {
    private final InputPortExtender inputPortExtender = new InputPortExtender("Input", getInputPorts());
    private final OutputPort outputPort = getOutputPorts().createPort("Output");
    public StepEndOperator(OperatorDescription description) {
        super(description);
        inputPortExtender.ensureMinimumNumberOfPorts(2);
        inputPortExtender.start();
    }

    public int findDiff(List<AbstractNode> tree1,List<AbstractNode> tree2,int index1,int index2){
        AbstractNode node1=tree1.get(index1);
        AbstractNode node2=tree2.get(index2);
        for(AbstractNode son:node2.YesSon) {
            if (!node1.YesSon.contains(son)) {
                return son.getIndex();
            }
        }
        for(AbstractNode son:node2.NoSon) {
            if (!node1.NoSon.contains(son)) {
                return son.getIndex();
            }
        }
        int diff=-1;
        for (AbstractNode son:node2.YesSon){
            diff=findDiff(tree1,tree2,tree1.indexOf(son),son.getIndex());
            if(diff!=-1){
                return diff;
            }
        }
        for (AbstractNode son:node2.NoSon){
            diff=findDiff(tree1,tree2,tree1.indexOf(son),son.getIndex());
            if(diff!=-1){
                return diff;
            }
        }
        return diff;
    }

    public List<AbstractNode> mergeTree(List<AbstractNode> tree1, List<AbstractNode> tree2){

        int diff=findDiff(tree1,tree2,0,0);

        if(diff==-1){
            return tree1;
        }
        // insert sons
        tree1=addSubTree(tree1,tree2,diff);
        return tree1;
    }

    public List<AbstractNode> addSubTree(List<AbstractNode> tree1,List<AbstractNode> tree2, int index){
        AbstractNode diff_node=tree2.get(index);
        tree1.add(diff_node);
//        Set<AbstractNode> parents=diff_node.parents;
//        for(AbstractNode parent:parents){
//            int id=parent.getIndex();
//            if(tree2.get(id).YesSon.contains(diff_node)){
//                tree1.get(id).YesSon.add(diff_node);
//            }else {
//                tree1.get(id).NoSon.add(diff_node);
//            }
//        }
        for (AbstractNode son: diff_node.YesSon){
            addSubTree(tree1,tree2,son.getIndex());
        }
        for (AbstractNode son: diff_node.NoSon){
            addSubTree(tree1,tree2,son.getIndex());
        }

        diff_node.runCheck();
        return tree1;
    }


    @Override
    public void doWork() throws OperatorException {
//        merge: 不再按列表顺序一个个检查，按深度优先搜索
//
//        一共两棵树，首先匹配根节点，一致后匹配子节点，首先对于所有根节点的子节点，如果有节点在被动树上而不在模版树，则直接归并所有子子节点并继续检查
//
//        如果所有节点都同时存在于两边，则对每棵子树再次检查
//
//        保证每次用被动树更新模版树


        List<Pack> packs=inputPortExtender.getData(Pack.class,true);
        Pack pack1=packs.get(0);
        pack1.current_parents.clear();
        List<AbstractNode> tree1=pack1.getModel().getLastStep().nodes;
        for (Pack pack:packs){
            List<AbstractNode> tree2=pack.getModel().getLastStep().nodes;
            tree1=mergeTree(tree1,tree2);
            pack1.getModel().getLastStep().flushIndex();
        }
        outputPort.deliver(pack1);
    }
}
