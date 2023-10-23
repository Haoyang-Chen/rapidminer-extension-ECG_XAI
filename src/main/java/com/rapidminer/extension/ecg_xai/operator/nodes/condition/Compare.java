package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

import java.util.ArrayList;
import java.util.List;

public class Compare extends AbstractCondition{
    private String leftOperand;
    private String operator;
    private String rightOperand;
    private String lead=null;

    public Compare() {
        super();
    }

    public Compare(String leftOperand, String operator, String rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public Compare(String leftOperand, String operator, String rightOperand,String lead) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.lead=lead;
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

    public String getLead() {
        return lead;
    }

    @Override
    public String toString() {
        if (lead==null) {
            return leftOperand + " " + operator + " " + rightOperand;
        }else {
            return leftOperand + " " + operator + " " + rightOperand+" in "+lead;
        }
    }

    public static void main(String[] args) {
        Compare compare = new Compare("A", ">", "B");
        System.out.println(compare);
    }
}
