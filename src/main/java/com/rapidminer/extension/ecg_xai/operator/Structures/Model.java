package com.rapidminer.extension.ecg_xai.operator.Structures;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.StartNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model {
    public List<Step> steps=new ArrayList<>();

    public Model(){

    }

    public Model(Model model){
        for (Step step: model.steps){
            this.steps.add(new Step(step));
        }
    }

    public void addStep(Step step){
        steps.add(step);
    }

    public Step getLastStep(){
        return steps.get(steps.size()-1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Model \n");
        for (Step step : steps) {
            sb.append("Step").append(steps.indexOf(step) + 1).append(": \n{\n");
            sb.append(step.toString()).append("\n }, \n \n");
        }
        if (!steps.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Model model = new Model();
        Step step1 = new Step();
        Compare compare=new Compare("A",">","C");
        ConditionNode conditionNode = new ConditionNode(compare);
        conditionNode.addParent(step1.getLast(),true);
        step1.addNode(conditionNode);

        Step step2 = new Step();
        Compare compare2=new Compare("A",">","C");
        ConditionNode conditionNode2 = new ConditionNode(compare2);
        conditionNode2.addParent(step2.getLast(),true);
        step2.addNode(conditionNode2);

        model.addStep(step1);
        model.addStep(step2);
        System.out.println(model);
    }

//    public Set<String> getResults(){
//        Set<String> results=new HashSet<>();
//        for (Step step:steps){
//            for (AbstractNode node:step.nodes){
//                if (node instanceof ImpressionNode){
//                    results.add("'"+node.getImpression()+"'");
//                }
//            }
//        }
//        return results;
//    }

//    public Set<String> getMidOutput(){
//        Set<String> midOutput=new HashSet<>();
//        Set<String> temp=new HashSet<>();
//        for (Step step:steps){
//            for (AbstractNode node:step.nodes){
//                if (!(node instanceof StartNode || node instanceof ImpressionNode)){
//                    midOutput.addAll(node.getMidOutput());
//                }
//            }
//        }
//        for (String mid:midOutput){
//            temp.add("'" + mid + "'");
//        }
//        return temp;
//    }

    public List<Step> getSteps() {
        return steps;
    }
}
