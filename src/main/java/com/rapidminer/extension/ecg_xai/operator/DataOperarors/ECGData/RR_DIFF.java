package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_General;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class RR_DIFF extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("RR_DIFF RR interval");

    public RR_DIFF(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="RR_DIFF";
        StringInfo_General data=new StringInfo_General(name);
        DataOutput.deliver(data);
    }
}
