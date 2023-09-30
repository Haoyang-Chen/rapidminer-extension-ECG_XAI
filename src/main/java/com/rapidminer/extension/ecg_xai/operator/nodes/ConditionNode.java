package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;

public class ConditionNode extends AbstractNode{
    private AbstractCondition condition;

    public ConditionNode(){
        setType("Condition");
    }

    public void setCondition(AbstractCondition condition) {
        this.condition=condition;
    }
}
