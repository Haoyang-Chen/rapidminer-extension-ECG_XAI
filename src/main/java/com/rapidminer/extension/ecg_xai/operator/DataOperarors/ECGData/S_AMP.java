package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_Lead;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class S_AMP extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("S_AMP S wave amplitude");

    public S_AMP(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="S_AMP S wave amplitude";
        StringInfo_Lead data=new StringInfo_Lead(name);
        DataOutput.deliver(data);
    }
}
