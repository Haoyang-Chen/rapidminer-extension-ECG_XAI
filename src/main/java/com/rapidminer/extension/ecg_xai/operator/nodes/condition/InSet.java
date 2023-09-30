package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

import java.util.HashSet;
import java.util.Set;

public class InSet extends AbstractCondition{
    private String element;
    private Set<String> set;

    public InSet(String element, Set<String> set) {
        this.element = element;
        this.set = set;
    }

    public String getElement() {
        return element;
    }

    public Set<String> getSet() {
        return set;
    }

    @Override
    public String toString() {
        return element + " in " + set;
    }

    public static void main(String[] args) {
        Set<String> stringSet = new HashSet<>();
        stringSet.add("A");
        stringSet.add("B");
        stringSet.add("C");

        InSet inSet = new InSet("X", stringSet);
        System.out.println(inSet);
    }
}
