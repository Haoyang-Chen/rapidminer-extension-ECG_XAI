package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class Exist extends AbstractCondition{
    private String element;
    private String lead=null;

    public Exist() {
        super();
    }

    public Exist(String element) {
        this.element = element;
    }
    public Exist(String element, String lead) {
        this.element = element;
        this.lead=lead;
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

    public static void main(String[] args) {
        Exist exist = new Exist("A");
        System.out.println(exist);
    }
}
