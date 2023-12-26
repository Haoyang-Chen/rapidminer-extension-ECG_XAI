package com.rapidminer.extension.ecg_xai.operator.superOperator.Step5;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step5Operator extends AbstractStepOperator {
    private final InputPort Q_DUROutput=getInputPorts().createPort("Q_DUR Q wave duration");
    private final OutputPort Q_DURInput = getSubprocess(0).getInnerSources().createPort("Q_DUR Q wave duration");
    private final InputPort Q_AMPOutput=getInputPorts().createPort("Q_AMP Q wave amplitude");
    private final OutputPort Q_AMPInput = getSubprocess(0).getInnerSources().createPort("Q_AMP Q wave amplitude");
    private final InputPort PRWPOutput=getInputPorts().createPort("PRWP Poor R wave progression");
    private final OutputPort PRWPInput = getSubprocess(0).getInnerSources().createPort("PRWP Poor R wave progression");

    //IMI
    private final InputPort IMI_STEInput = getSubprocess(0).getInnerSinks().createPort("IMI_STE acute inferior myocardial infarction");
    private final OutputPort IMI_STEOutput=getOutputPorts().createPort("IMI_STE acute inferior myocardial infarction");
    //LMI
    private final InputPort LMI_STEInput = getSubprocess(0).getInnerSinks().createPort("LMI_STE acute lateral myocardial infarction");
    private final OutputPort LMI_STEOutput=getOutputPorts().createPort("LMI_STE acute lateral myocardial infarction");
    //AMI_QP
    private final InputPort AMI_QPInput = getSubprocess(0).getInnerSinks().createPort("AMI_QP acute anterior myocardial infarction");
    private final OutputPort AMI_QPOutput=getOutputPorts().createPort("AMI_QP acute anterior myocardial infarction");
    //AMI_PRWP
    private final InputPort AMI_PRWPInput = getSubprocess(0).getInnerSinks().createPort("AMI_PRWP acute anterior myocardial infarction");
    private final OutputPort AMI_PRWPOutput=getOutputPorts().createPort("AMI_PRWP acute anterior myocardial infarction");
    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH left ventricular hypertrophy");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH left ventricular hypertrophy");
    //LBBB
    private final InputPort LBBBInput = getSubprocess(0).getInnerSinks().createPort("LBBB left bundle branch block");
    private final OutputPort LBBBOutput=getOutputPorts().createPort("LBBB left bundle branch block");


    public Step5Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(Q_DUROutput,Q_DURInput, false));
        getTransformer().addRule(new PassThroughRule(Q_AMPOutput, Q_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(PRWPOutput, PRWPInput, false));
        getTransformer().addRule(new PassThroughRule(IMI_STEInput, IMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(LMI_STEInput, LMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(AMI_QPInput, AMI_QPOutput, false));
        getTransformer().addRule(new PassThroughRule(AMI_PRWPInput, AMI_PRWPOutput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
        getTransformer().addRule(new PassThroughRule(LBBBInput, LBBBOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        Q_DURInput.deliver(Q_DUROutput.getData(StringInfo.class));
        Q_AMPInput.deliver(Q_AMPOutput.getData(StringInfo.class));
        PRWPInput.deliver(PRWPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutSummaryOutput.deliver(getSubprocess(0).getAllInnerOperators().get(0).getOutputPorts().getPortByIndex(0).getData(Pack.class));
        IMI_STEOutput.deliver(IMI_STEInput.getData(StringInfo.class));
        LMI_STEOutput.deliver(LMI_STEInput.getData(StringInfo.class));
        AMI_QPOutput.deliver(AMI_QPInput.getData(StringInfo.class));
        AMI_PRWPOutput.deliver(AMI_PRWPInput.getData(StringInfo.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo.class));
        LBBBOutput.deliver(LBBBInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
