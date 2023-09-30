package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

public class Exist extends AbstractCondition{
    private String element;

    public Exist(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    @Override
    public String toString() {
        return "Exist(" + element + ")";
    }

    public static void main(String[] args) {
        Exist exist = new Exist("A");
        System.out.println(exist);
    }
}
