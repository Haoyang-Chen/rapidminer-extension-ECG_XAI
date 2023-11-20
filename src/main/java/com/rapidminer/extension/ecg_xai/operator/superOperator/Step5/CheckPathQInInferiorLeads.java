
package com.rapidminer.extension.ecg_xai.operator.superOperator.Step5;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class CheckPathQInInferiorLeads extends AbstractStepOperator {
    private final InputPort Q_DUROutput=getInputPorts().createPort("Q_DUR Q wave duration");
    private final OutputPort Q_DURInput = getSubprocess(0).getInnerSources().createPort("Q_DUR Q wave duration");
    private final InputPort Q_AMPOutput=getInputPorts().createPort("Q_AMP Q wave amplitude");
    private final OutputPort Q_AMPInput = getSubprocess(0).getInnerSources().createPort("Q_AMP Q wave amplitude");
    //IMI
    private final InputPort IMI_STEInput = getSubprocess(0).getInnerSinks().createPort("IMI_STE acute inferior myocardial infarction");
    private final OutputPort IMI_STEOutput=getOutputPorts().createPort("IMI_STE acute inferior myocardial infarction");
    //LMI



    public CheckPathQInInferiorLeads(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(Q_DUROutput,Q_DURInput, false));
        getTransformer().addRule(new PassThroughRule(Q_AMPOutput, Q_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(IMI_STEInput, IMI_STEOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        Q_DURInput.deliver(Q_DUROutput.getData(StringInfo.class));
        Q_AMPInput.deliver(Q_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        IMI_STEOutput.deliver(IMI_STEInput.getData(Pack.class));
        outExtender.collect();
    }
}
