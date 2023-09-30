package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.StartNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class Step {
    public List<AbstractNode> nodes=new ArrayList<>();

    public Step(){
        StartNode firstNode=new StartNode();
        nodes.add(firstNode);
    }

    public void addNode(AbstractNode node){
        nodes.add(node);
    }

    public AbstractNode getLast(){
        return nodes.get(nodes.size()-1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Nodes: \n");
        for (AbstractNode node : nodes) {
            sb.append(node.toString()).append(", \n");
        }
        if (!nodes.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Step step = new Step();
        Compare compare=new Compare("A",">","C");
        ConditionNode conditionNode = new ConditionNode(compare);
        conditionNode.addParent(step.getLast(),true);
        step.addNode(conditionNode);
        System.out.println(step);
    }
}
