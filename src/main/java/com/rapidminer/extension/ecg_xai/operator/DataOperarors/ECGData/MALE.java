package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo_General;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.GenerateNewMDRule;

public class MALE extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("MALE");

    public MALE(OperatorDescription description) {
        super(description);
        getTransformer().addRule(new GenerateNewMDRule(DataOutput, StringInfo.class));
    }

    @Override
    public void doWork() throws OperatorException {
        String name="MALE";
        StringInfo_General data=new StringInfo_General(name);
        DataOutput.deliver(data);
    }
}
