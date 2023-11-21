package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

import java.util.HashSet;
import java.util.Set;

public class InSet extends AbstractCondition{
    private String element;
    private String set=null;

    public InSet() {
        super();
    }

    public InSet(String element) {
        super();
        this.element = element;
    }
    public InSet(String element, String set) {
        super();
        this.element = element;
        this.set = set;
    }

    public String getElement() {
        return element;
    }

    public String getSet() {
        return set;
    }

    @Override
    public String toString() {
        if (set==null) {
            return element;
        }
        return element + " in " + set;
    }

    public static void main(String[] args) {
        InSet inSet = new InSet("A");
        System.out.println(inSet);
    }

    public String getLeftOperand() {
        return null;
    }

    public String getOperator() {
        return null;
    }

    public String getRightOperand() {
        return null;
    }
}