package com.rapidminer.extension.ecg_xai.operator.superOperator.Step6;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step6Operator extends AbstractStepOperator {
    private final InputPort P_DUROutput=getInputPorts().createPort("P_DUR P wave duration");
    private final OutputPort P_DURInput = getSubprocess(0).getInnerSources().createPort("P_DUR P wave duration");
    private final InputPort P_AMPOutput=getInputPorts().createPort("P_AMP P wave amplitude");
    private final OutputPort P_AMPInput = getSubprocess(0).getInnerSources().createPort("P_AMP P wave amplitude");


    //LAE
    private final InputPort LAEInput = getSubprocess(0).getInnerSinks().createPort("LAE left atrial enlargement");
    private final OutputPort LAEOutput=getOutputPorts().createPort("LAE left atrial enlargement");
    //RAE
    private final InputPort RAEInput = getSubprocess(0).getInnerSinks().createPort("RAE right atrial enlargement");
    private final OutputPort RAEOutput=getOutputPorts().createPort("RAE right atrial enlargement");
    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH left ventricular hypertrophy");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH left ventricular hypertrophy");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH right ventricular hypertrophy");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH right ventricular hypertrophy");


    public Step6Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(P_DUROutput,P_DURInput, false));
        getTransformer().addRule(new PassThroughRule(P_AMPOutput, P_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(LAEInput, LAEOutput, false));
        getTransformer().addRule(new PassThroughRule(RAEInput, RAEOutput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
        getTransformer().addRule(new PassThroughRule(RVHInput, RVHOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        P_DURInput.deliver(P_DUROutput.getData(StringInfo.class));
        P_AMPInput.deliver(P_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        LAEOutput.deliver(LAEInput.getData(StringInfo.class));
        RAEOutput.deliver(RAEInput.getData(StringInfo.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo.class));
        RVHOutput.deliver(RVHInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
