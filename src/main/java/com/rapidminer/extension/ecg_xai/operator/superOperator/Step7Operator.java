package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo_General;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step7Operator extends AbstractStepOperator {
    private final InputPort AGEOutput=getInputPorts().createPort("AGE");
    private final OutputPort AGEInput = getSubprocess(0).getInnerSources().createPort("AGE");
    private final InputPort MALEOutput=getInputPorts().createPort("MALE");
    private final OutputPort MALEInput = getSubprocess(0).getInnerSources().createPort("MALE");
    private final InputPort R_AMPOutput=getInputPorts().createPort("R_AMP");
    private final OutputPort R_AMPInput = getSubprocess(0).getInnerSources().createPort("R_AMP");
    private final InputPort S_AMPOutput=getInputPorts().createPort("S_AMP");
    private final OutputPort S_AMPInput = getSubprocess(0).getInnerSources().createPort("S_AMP");
    private final InputPort RS_RATIOOutput=getInputPorts().createPort("RS_RATIO");
    private final OutputPort RS_RATIOInput = getSubprocess(0).getInnerSources().createPort("RS_RATIO");
    private final InputPort RADOutput=getInputPorts().createPort("RAD");
    private final OutputPort RADInput = getSubprocess(0).getInnerSources().createPort("RAD");


    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH");


    public Step7Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(AGEOutput,AGEInput, false));
        getTransformer().addRule(new PassThroughRule(MALEOutput,MALEInput, false));
        getTransformer().addRule(new PassThroughRule(R_AMPOutput, R_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(S_AMPOutput, S_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(RS_RATIOOutput, RS_RATIOInput, false));
        getTransformer().addRule(new PassThroughRule(RADOutput, RADInput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
        getTransformer().addRule(new PassThroughRule(RVHInput, RVHOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        AGEInput.deliver(AGEOutput.getData(StringInfo_General.class));
        MALEInput.deliver(MALEOutput.getData(StringInfo_General.class));
        R_AMPInput.deliver(R_AMPOutput.getData(StringInfo_General.class));
        S_AMPInput.deliver(S_AMPOutput.getData(StringInfo_General.class));
        RS_RATIOInput.deliver(RS_RATIOOutput.getData(StringInfo_General.class));
        RADInput.deliver(RADOutput.getData(StringInfo_General.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo_General.class));
        RVHOutput.deliver(RVHInput.getData(StringInfo_General.class));
        outExtender.collect();
    }
}
