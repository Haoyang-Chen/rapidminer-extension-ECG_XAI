package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class ConditionGroup extends AbstractCondition{
    private AbstractCondition left;
    private AbstractCondition right;
    private String relation;

    public ConditionGroup(AbstractCondition left, AbstractCondition right, String operator) {
        this.left = left;
        this.right = right;
        this.relation = operator;
    }

    public String getOperator() {
        return relation;
    }

    public AbstractCondition getLeft() {
        return left;
    }

    public AbstractCondition getRight() {
        return right;
    }
}
