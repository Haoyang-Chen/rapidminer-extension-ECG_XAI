package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

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
}
