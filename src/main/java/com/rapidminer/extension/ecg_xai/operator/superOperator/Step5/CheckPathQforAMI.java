package com.rapidminer.extension.ecg_xai.operator.superOperator.Step5;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class CheckPathQforAMI extends AbstractStepOperator {
    private final InputPort Q_DUROutput=getInputPorts().createPort("Q_DUR Q wave duration");
    private final OutputPort Q_DURInput = getSubprocess(0).getInnerSources().createPort("Q_DUR Q wave duration");
    private final InputPort Q_AMPOutput=getInputPorts().createPort("Q_AMP Q wave amplitude");
    private final OutputPort Q_AMPInput = getSubprocess(0).getInnerSources().createPort("Q_AMP Q wave amplitude");

    private final InputPort AMI_QPInput = getSubprocess(0).getInnerSinks().createPort("AMI_QP acute anterior myocardial infarction");
    private final OutputPort AMI_QPOutput=getOutputPorts().createPort("AMI_QP acute anterior myocardial infarction");



    public CheckPathQforAMI(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(Q_DUROutput,Q_DURInput, false));
        getTransformer().addRule(new PassThroughRule(Q_AMPOutput, Q_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(AMI_QPInput, AMI_QPOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        Q_DURInput.deliver(Q_DUROutput.getData(StringInfo.class));
        Q_AMPInput.deliver(Q_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        AMI_QPOutput.deliver(AMI_QPInput.getData(Pack.class));
        outExtender.collect();
    }
}
