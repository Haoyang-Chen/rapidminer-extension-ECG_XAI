package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

abstract public class AbstractCondition {
    protected String resultName="none";

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getResultName() {
        return resultName;
    }

    abstract public String toString();
}
