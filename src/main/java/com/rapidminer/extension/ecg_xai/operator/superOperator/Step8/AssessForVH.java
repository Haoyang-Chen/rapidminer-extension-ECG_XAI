package com.rapidminer.extension.ecg_xai.operator.superOperator.Step8;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class AssessForVH extends AbstractStepOperator {
    private final InputPort T_AMPOutput=getInputPorts().createPort("T_AMP T wave amplitude");
    private final OutputPort T_AMPInput = getSubprocess(0).getInnerSources().createPort("T_AMP T wave amplitude");

    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH left ventricular hypertrophy");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH left ventricular hypertrophy");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH right ventricular hypertrophy");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH right ventricular hypertrophy");


    public AssessForVH(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(T_AMPOutput,T_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
        getTransformer().addRule(new PassThroughRule(RVHInput, RVHOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        T_AMPInput.deliver(T_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        LVHOutput.deliver(LVHInput.getData(Pack.class));
        RVHOutput.deliver(RVHInput.getData(Pack.class));
        outExtender.collect();
    }
}
