package com.rapidminer.extension.ecg_xai.operator.superOperator.Step7;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class AssessForRVH extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    private final InputPort R_AMPOutput=getInputPorts().createPort("R_AMP R wave amplitude");
    private final OutputPort R_AMPInput = getSubprocess(0).getInnerSources().createPort("R_AMP R wave amplitude");
    private final InputPort S_AMPOutput=getInputPorts().createPort("S_AMP S wave amplitude");
    private final OutputPort S_AMPInput = getSubprocess(0).getInnerSources().createPort("S_AMP S wave amplitude");
    private final InputPort RS_RATIOOutput=getInputPorts().createPort("RS_RATIO R/S wave amplitude ratio");
    private final OutputPort RS_RATIOInput = getSubprocess(0).getInnerSources().createPort("RS_RATIO R/S wave amplitude ratio");
    private final InputPort RADOutput=getInputPorts().createPort("RAD right axis deviation");
    private final OutputPort RADInput = getSubprocess(0).getInnerSources().createPort("RAD right axis deviation");


    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH right ventricular hypertrophy");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH right ventricular hypertrophy");


    public AssessForRVH(OperatorDescription description) {
        super(description,"Executed Process");
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(R_AMPOutput, R_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(S_AMPOutput, S_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(RS_RATIOOutput, RS_RATIOInput, false));
        getTransformer().addRule(new PassThroughRule(RADOutput, RADInput, false));
        getTransformer().addRule(new PassThroughRule(RVHInput, RVHOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        R_AMPInput.deliver(R_AMPOutput.getData(StringInfo.class));
        S_AMPInput.deliver(S_AMPOutput.getData(StringInfo.class));
        RS_RATIOInput.deliver(RS_RATIOOutput.getData(StringInfo.class));
        RADInput.deliver(RADOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        RVHOutput.deliver(RVHInput.getData(Pack.class));
    }
}
