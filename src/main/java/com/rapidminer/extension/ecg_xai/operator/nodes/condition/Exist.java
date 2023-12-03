package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class Exist extends AbstractCondition{
    private String element;
    private String lead=null;

    public Exist() {
        super();
        this.type="Exist";
    }

    public Exist(String element) {
        super();
        this.type="Exist";
        this.element = element;
    }
    public Exist(String element, String lead) {
        super();
        this.type="Exist";
        this.element = element;
        this.lead=lead;
    }

    public String getResultName(){
        return element;
    }

    @Override
    public String getLead() {
        return lead;
    }

    public String getElement() {
        return element;
    }

    @Override
    public String toString() {
        if (lead==null) {
            return element + " is true";
        }else {
            return element+"_"+lead + " is true";
        }
    }

    @Override
    public String getFeature() {
        return element;
    }

    @Override
    public String getOperator() {
        return null;
    }

    @Override
    public String getThreshold() {
        return null;
    }

    public static void main(String[] args) {
        Exist exist = new Exist("A");
        System.out.println(exist);
    }

    public String getLeftOperand() {
        return null;
    }

    public String getMidOperand() {
        return null;
    }

    public String getRightOperand() {
        return null;
    }
}
