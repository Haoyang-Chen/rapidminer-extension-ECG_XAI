package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Exist;

public class ConditionNode extends AbstractNode{
    private AbstractCondition condition;

    public ConditionNode(AbstractCondition condition){
        setType("Condition");
        this.condition=condition;
    }

    public void setCondition(AbstractCondition condition) {
        this.condition=condition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndex().toString()).append(": ");
        sb.append("[ConditionNode], ").append(condition);
        sb.append(", ").append(condition.getResultName()).append(", [");

        for (AbstractNode parent : parents) {
            sb.append(parent.getIndex()).append(", ");
        }

        if (!parents.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]");
        sb.append(", [");
        for (AbstractNode yesSon : YesSon) {
            sb.append(yesSon.getIndex()).append(", ");
        }
        if (!YesSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append(", [");
        for (AbstractNode noSon : NoSon) {
            sb.append(noSon.getIndex()).append(", ");
        }
        if (!NoSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Exist condition = new Exist("A");
        ConditionNode conditionNode = new ConditionNode(condition);
        System.out.println(conditionNode);
    }
}
