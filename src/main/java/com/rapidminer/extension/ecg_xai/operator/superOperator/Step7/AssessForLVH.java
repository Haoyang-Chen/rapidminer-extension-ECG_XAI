package com.rapidminer.extension.ecg_xai.operator.superOperator.Step7;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class AssessForLVH extends AbstractStepOperator {
    private final InputPort AGEOutput=getInputPorts().createPort("AGE");
    private final OutputPort AGEInput = getSubprocess(0).getInnerSources().createPort("AGE");
    private final InputPort MALEOutput=getInputPorts().createPort("MALE");
    private final OutputPort MALEInput = getSubprocess(0).getInnerSources().createPort("MALE");
    private final InputPort R_AMPOutput=getInputPorts().createPort("R_AMP R wave amplitude");
    private final OutputPort R_AMPInput = getSubprocess(0).getInnerSources().createPort("R_AMP R wave amplitude");
    private final InputPort S_AMPOutput=getInputPorts().createPort("S_AMP S wave amplitude");
    private final OutputPort S_AMPInput = getSubprocess(0).getInnerSources().createPort("S_AMP S wave amplitude");


    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH left ventricular hypertrophy");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH left ventricular hypertrophy");


    public AssessForLVH(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(AGEOutput,AGEInput, false));
        getTransformer().addRule(new PassThroughRule(MALEOutput,MALEInput, false));
        getTransformer().addRule(new PassThroughRule(R_AMPOutput, R_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(S_AMPOutput, S_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        AGEInput.deliver(AGEOutput.getData(StringInfo.class));
        MALEInput.deliver(MALEOutput.getData(StringInfo.class));
        R_AMPInput.deliver(R_AMPOutput.getData(StringInfo.class));
        S_AMPInput.deliver(S_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        LVHOutput.deliver(LVHInput.getData(Pack.class));
        outExtender.collect();
    }
}
