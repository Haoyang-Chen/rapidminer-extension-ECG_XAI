package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class Compare extends AbstractCondition{
    private String leftOperand;
    private String operator;
    private String rightOperand;

    public Compare(String leftOperand, String operator, String rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public String getOperator() {
        return operator;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return leftOperand + " " + operator + " " + rightOperand;
    }

    public static void main(String[] args) {
        Compare compare = new Compare("A", ">", "B");
        System.out.println(compare);
    }
}
