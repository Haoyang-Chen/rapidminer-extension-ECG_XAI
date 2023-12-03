package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class DoubleCompare extends AbstractCondition{
    private String leftOperand;
    private String midOperand;
    private String rightOperand;
    private String lead=null;

    public DoubleCompare() {
        super();
        this.type="DoubleCompare";
    }

    public DoubleCompare(String leftOperand, String operator, String rightOperand) {
        super();
        this.type="DoubleCompare";
        this.leftOperand = leftOperand;
        this.midOperand = operator;
        this.rightOperand = rightOperand;
    }

    public DoubleCompare(String leftOperand, String operator, String rightOperand, String lead) {
        super();
        this.type="DoubleCompare";
        this.leftOperand = leftOperand;
        this.midOperand = operator;
        this.rightOperand = rightOperand;
        this.lead=lead;
    }

    public String getFeature() {
    	return midOperand;
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
        return leftOperand;
    }

    public String getMidOperand() {
        return midOperand;
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
            return leftOperand + " " + midOperand + " " + rightOperand;
        }else {
            return leftOperand+"_"+lead + " " + midOperand + " " + rightOperand;
        }
    }

    public static void main(String[] args) {
        DoubleCompare compare = new DoubleCompare("A", ">", "B");
        System.out.println(compare);
    }
}
