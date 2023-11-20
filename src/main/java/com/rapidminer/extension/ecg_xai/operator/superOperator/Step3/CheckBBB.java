package com.rapidminer.extension.ecg_xai.operator.superOperator.Step3;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class CheckBBB extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    private final InputPort LBBBOutput=getInputPorts().createPort("LBBB left bundle branch block");
    private final OutputPort LBBBInput = getSubprocess(0).getInnerSources().createPort("LBBB left bundle branch block");
    private final InputPort RBBBOutput=getInputPorts().createPort("RBBB right bundle branch block");
    private final OutputPort RBBBInput = getSubprocess(0).getInnerSources().createPort("RBBB right bundle branch block");
    public final InputPort YesOutModelInput=getSubprocess(0).getInnerSinks().createPort("Yes");
    public final OutputPort YesOutModelOutput=getOutputPorts().createPort("Yes");
    public final InputPort NoOutModelInput=getSubprocess(0).getInnerSinks().createPort("No");
    public final OutputPort NoOutModelOutput=getOutputPorts().createPort("No");

    public CheckBBB(OperatorDescription description) {
        super(description,"Executed Process");
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(LBBBOutput, LBBBInput, false));
        getTransformer().addRule(new PassThroughRule(RBBBOutput, RBBBInput, false));
        getTransformer().addRule(new PassThroughRule(YesOutModelInput, YesOutModelOutput, false));
        getTransformer().addRule(new PassThroughRule(NoOutModelInput, NoOutModelOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        LBBBInput.deliver(LBBBOutput.getData(StringInfo.class));
        RBBBInput.deliver(RBBBOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        YesOutModelOutput.deliver(YesOutModelInput.getData(Pack.class));
        NoOutModelOutput.deliver(NoOutModelInput.getData(Pack.class));
    }
}
