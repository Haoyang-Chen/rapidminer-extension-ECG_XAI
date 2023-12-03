package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

abstract public class AbstractCondition {
    protected String resultName="none";

    public String type="none";

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getResultName() {
        return resultName;
    }

    abstract public String toString();

    abstract public String getFeature();
    abstract public String getOperator();
    abstract public String getThreshold();

    abstract public String getLeftOperand();
    abstract public String getMidOperand();
    abstract public String getRightOperand();
}
