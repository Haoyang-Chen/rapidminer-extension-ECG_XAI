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
        this.type="Compare";
    }

    public Compare(String leftOperand, String operator, String rightOperand) {
        super();
        this.type="Compare";
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

    public String getFeature() {
    	return leftOperand;
    }

    public String getOperator() {
    	return operator;
    }

    public String getThreshold() {
    	return rightOperand;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public String getMidOperand() {
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
            return leftOperand+"_"+lead + " " + operator + " " + rightOperand;
        }
    }

    public static void main(String[] args) {
        Compare compare = new Compare("A", ">", "B");
        System.out.println(compare);
    }
}
