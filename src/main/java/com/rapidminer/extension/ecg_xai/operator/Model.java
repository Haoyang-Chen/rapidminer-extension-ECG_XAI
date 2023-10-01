package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public List<Step> steps=new ArrayList<>();

    public void addStep(Step step){
        steps.add(step);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Model [ ");
        for (Step step : steps) {
            sb.append("{\n Step"+(steps.indexOf(step)+1)+": ");
            sb.append(step.toString()).append("\n }, \n");
        }
        if (!steps.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
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
}
