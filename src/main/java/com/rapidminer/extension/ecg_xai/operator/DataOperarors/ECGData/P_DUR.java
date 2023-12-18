package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_Lead;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class P_DUR extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("P_DUR P wave duration");

    public P_DUR(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="P_DUR P wave duration";
        StringInfo_Lead data=new StringInfo_Lead(name);
        DataOutput.deliver(data);
    }
}
