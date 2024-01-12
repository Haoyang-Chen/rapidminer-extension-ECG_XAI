package com.rapidminer.extension.ecg_xai.operator.DataOperarors.ECGData;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo_Lead;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.GenerateNewMDRule;

public class RS_RATIO extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("RS_RATIO R/S wave amplitude ratio");

    public RS_RATIO(OperatorDescription description) {
        super(description);
        getTransformer().addRule(new GenerateNewMDRule(DataOutput, StringInfo.class));
    }

    @Override
    public void doWork() throws OperatorException {
        String name="RS_RATIO";
        StringInfo_Lead data=new StringInfo_Lead(name);
        DataOutput.deliver(data);
    }
}
