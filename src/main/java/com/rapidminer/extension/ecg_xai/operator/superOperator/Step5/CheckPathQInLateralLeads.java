package com.rapidminer.extension.ecg_xai.operator.superOperator.Step5;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class CheckPathQInLateralLeads extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    private final InputPort Q_DUROutput=getInputPorts().createPort("Q_DUR Q wave duration");
    private final OutputPort Q_DURInput = getSubprocess(0).getInnerSources().createPort("Q_DUR Q wave duration");
    private final InputPort Q_AMPOutput=getInputPorts().createPort("Q_AMP Q wave amplitude");
    private final OutputPort Q_AMPInput = getSubprocess(0).getInnerSources().createPort("Q_AMP Q wave amplitude");

    private final InputPort LMI_STEInput = getSubprocess(0).getInnerSinks().createPort("LMI_STE acute lateral myocardial infarction");
    private final OutputPort LMI_STEOutput=getOutputPorts().createPort("LMI_STE acute lateral myocardial infarction");



    public CheckPathQInLateralLeads(OperatorDescription description) {
        super(description,"Executed Process");
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(Q_DUROutput,Q_DURInput, false));
        getTransformer().addRule(new PassThroughRule(Q_AMPOutput, Q_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(LMI_STEInput, LMI_STEOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        Q_DURInput.deliver(Q_DUROutput.getData(StringInfo.class));
        Q_AMPInput.deliver(Q_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        LMI_STEOutput.deliver(LMI_STEInput.getData(Pack.class));
    }
}
