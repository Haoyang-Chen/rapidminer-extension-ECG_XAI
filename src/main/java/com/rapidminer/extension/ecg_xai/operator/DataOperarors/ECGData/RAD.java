package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_General;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class RAD extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("RAD right axis deviation");

    public RAD(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String name="RAD right axis deviation";
        StringInfo_General data=new StringInfo_General(name);
        DataOutput.deliver(data);
    }
}
