package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

abstract public class AbstractCondition {
    public String resultName="None";

    public String type="None";

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getResultName() {
        return resultName;
    }

    abstract public String getLead();

    abstract public String toString();

    abstract public String getFeature();

    abstract public String getOperator();
    abstract public String getThreshold();

    abstract public String getLeftOperand();
    abstract public String getMidOperand();
    abstract public String getRightOperand();
}
