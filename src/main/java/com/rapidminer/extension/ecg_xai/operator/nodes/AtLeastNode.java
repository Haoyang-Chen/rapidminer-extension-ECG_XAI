package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Exist;

import java.util.List;

public class AtLeastNode extends AbstractNode{
    private List<AbstractCondition> conditions;

    public AtLeastNode() {
        super();
        this.conditions = new java.util.ArrayList<>();
    }

    private int num;

    public void addCondition(AbstractCondition condition) {
        this.conditions.add(condition);
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndex().toString()).append(": ");
        sb.append("[AtLeastNode], ");
        sb.append("At least ").append(num).append(" of the conditions:\n");
        for (AbstractCondition condition : conditions) {
            sb.append(condition).append(", ").append(condition.getResultName()).append("\n");
        }
        sb.append(", [");

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
}
