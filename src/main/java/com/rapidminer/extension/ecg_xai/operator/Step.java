package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.StartNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Step {
    public List<AbstractNode> nodes=new ArrayList<>();
    public String focus_leads;
    public String name;

    public Step(){
        StartNode firstNode=new StartNode();
        nodes.add(firstNode);
    }

    public Step(Step step){
        this.nodes.addAll(step.nodes);
        this.focus_leads=step.focus_leads;
        this.name=step.name;
    }

    public void flushIndex(){
        for (int i=0;i<nodes.size();i++){
            nodes.get(i).setIndex(i);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNode(AbstractNode node){
        nodes.add(node);
        node.setIndex(nodes.size()-1);
    }

    public AbstractNode getLast(){
        return nodes.get(nodes.size()-1);
    }

    public AbstractNode getLastCon(){
        for (int i=this.nodes.size()-1;i>=0;i--){
            AbstractNode node=this.nodes.get(i);
            if (!Objects.equals(node.getType(), "Impression")){
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(focus_leads).append("\n");
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
