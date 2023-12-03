package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class ConditionGroup extends AbstractCondition{
    private AbstractCondition left;
    private AbstractCondition right;
    private String relation;

    public ConditionGroup(){
        super();
        this.type="ConditionGroup";
        left=null;
        right=null;
        relation=null;
    }
    public ConditionGroup(AbstractCondition left, AbstractCondition right, String operator) {
        super();
        this.type="ConditionGroup";
        this.left = left;
        this.right = right;
        this.relation = operator;
    }

    public void setLeft(AbstractCondition left) {
        this.left = left;
    }

    public void setRight(AbstractCondition right) {
        this.right = right;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMidOperand() {
        return relation;
    }

    public AbstractCondition getLeft() {
        return left;
    }

    public AbstractCondition getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + relation + " " + right + ")";
    }

    @Override
    public String getFeature() {
        return null;
    }

    @Override
    public String getOperator() {
        return null;
    }

    @Override
    public String getThreshold() {
        return null;
    }

    public String getLeftOperand() {
        return null;
    }

    public String getRightOperand() {
        return null;
    }

    public static void main(String[] args) {
        Compare condition1 = new Compare("A", ">", "B");
        Compare condition2 = new Compare("C", "<", "D");
        ConditionGroup group = new ConditionGroup(condition1, condition2, "AND");
        System.out.println(group);
    }
}
