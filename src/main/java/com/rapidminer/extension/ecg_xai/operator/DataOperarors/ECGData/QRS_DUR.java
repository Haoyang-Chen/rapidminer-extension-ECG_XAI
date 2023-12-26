package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_General;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class QRS_DUR extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("QRS_DUR QRS duration");

    public QRS_DUR(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="QRS_DUR";
        StringInfo_General data=new StringInfo_General(name);
        DataOutput.deliver(data);
    }
}
