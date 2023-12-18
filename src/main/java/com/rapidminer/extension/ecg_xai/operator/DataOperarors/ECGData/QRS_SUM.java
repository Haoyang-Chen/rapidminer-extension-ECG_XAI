package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_Lead;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class QRS_SUM extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("QRS_SUM QRS complex duration");

    public QRS_SUM(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="QRS_SUM QRS complex duration";
        StringInfo_Lead data=new StringInfo_Lead(name);
        DataOutput.deliver(data);
    }
}
