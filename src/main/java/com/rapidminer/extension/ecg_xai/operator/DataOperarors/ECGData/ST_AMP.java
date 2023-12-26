package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_General;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_Lead;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class ST_AMP extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("ST_AMP ST amplitude");

    public ST_AMP(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="ST_AMP";
        StringInfo_Lead data=new StringInfo_Lead(name);
        DataOutput.deliver(data);
    }
}
