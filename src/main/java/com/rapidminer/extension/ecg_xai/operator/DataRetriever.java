package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;

import java.util.List;

public class DataRetriever extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("Data");
    public static final String PARAMETER_NAME="Data Name";

    public DataRetriever(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name=getParameterAsString(PARAMETER_NAME);
        this.rename(name);
        DataOutput.deliver(new StringInfo(name));
    }
    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeString(PARAMETER_NAME,"Name of the Data",true));
        return types;
    }
}
