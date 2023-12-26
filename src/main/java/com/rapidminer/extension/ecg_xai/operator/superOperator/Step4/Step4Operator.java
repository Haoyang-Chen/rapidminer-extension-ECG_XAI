package com.rapidminer.extension.ecg_xai.operator.superOperator.Step4;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step4Operator extends AbstractStepOperator {
    private final InputPort ST_AMPOutput=getInputPorts().createPort("ST_AMP ST amplitude");
    private final OutputPort ST_AMPInput = getSubprocess(0).getInnerSources().createPort("ST_AMP ST amplitude");

    private final InputPort LMI_STEInput = getSubprocess(0).getInnerSinks().createPort("LMI_STE acute lateral myocardial infarction");
    private final OutputPort LMI_STEOutput=getOutputPorts().createPort("LMI_STE acute lateral myocardial infarction");
    private final InputPort AMI_STEInput = getSubprocess(0).getInnerSinks().createPort("AMI_STE acute anterior myocardial infarction");
    private final OutputPort AMI_STEOutput=getOutputPorts().createPort("AMI_STE acute anterior myocardial infarction");
    //IMI
    private final InputPort IMI_STEInput = getSubprocess(0).getInnerSinks().createPort("IMI_STE acute inferior myocardial infarction");
    private final OutputPort IMI_STEOutput=getOutputPorts().createPort("IMI_STE acute inferior myocardial infarction");
    //STE
    private final InputPort STEInput = getSubprocess(0).getInnerSinks().createPort("STE ST segment elevation");
    private final OutputPort STEOutput=getOutputPorts().createPort("STE ST segment elevation");
    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH left ventricular hypertrophy");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH left ventricular hypertrophy");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH right ventricular hypertrophy");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH right ventricular hypertrophy");
    //AMI_STD
    private final InputPort AMI_STDInput = getSubprocess(0).getInnerSinks().createPort("AMI_STD acute anterior myocardial infarction");
    private final OutputPort AMI_STDOutput=getOutputPorts().createPort("AMI_STD acute anterior myocardial infarction");
    //LMI_STD
    private final InputPort LMI_STDInput = getSubprocess(0).getInnerSinks().createPort("LMI_STD acute lateral myocardial infarction");
    private final OutputPort LMI_STDOutput=getOutputPorts().createPort("LMI_STD acute lateral myocardial infarction");
    //IMI_STD
    private final InputPort IMI_STDInput = getSubprocess(0).getInnerSinks().createPort("IMI_STD acute inferior myocardial infarction");
    private final OutputPort IMI_STDOutput=getOutputPorts().createPort("IMI_STD acute inferior myocardial infarction");
    //STD
    private final InputPort STDInput = getSubprocess(0).getInnerSinks().createPort("STD ST segment depression");
    private final OutputPort STDOutput=getOutputPorts().createPort("STD ST segment depression");


    public Step4Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(ST_AMPOutput,ST_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(LMI_STEInput, LMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(AMI_STEInput, AMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(IMI_STEInput, IMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(STEInput, STEOutput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
        getTransformer().addRule(new PassThroughRule(RVHInput, RVHOutput, false));
        getTransformer().addRule(new PassThroughRule(AMI_STDInput, AMI_STDOutput, false));
        getTransformer().addRule(new PassThroughRule(LMI_STDInput, LMI_STDOutput, false));
        getTransformer().addRule(new PassThroughRule(IMI_STDInput, IMI_STDOutput, false));
        getTransformer().addRule(new PassThroughRule(STDInput, STDOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        ST_AMPInput.deliver(ST_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutSummaryOutput.deliver(getSubprocess(0).getAllInnerOperators().get(0).getOutputPorts().getPortByIndex(0).getData(Pack.class));
        LMI_STEOutput.deliver(LMI_STEInput.getData(StringInfo.class));
        AMI_STEOutput.deliver(AMI_STEInput.getData(StringInfo.class));
        IMI_STEOutput.deliver(IMI_STEInput.getData(StringInfo.class));
        STEOutput.deliver(STEInput.getData(StringInfo.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo.class));
        RVHOutput.deliver(RVHInput.getData(StringInfo.class));
        AMI_STDOutput.deliver(AMI_STDInput.getData(StringInfo.class));
        LMI_STDOutput.deliver(LMI_STDInput.getData(StringInfo.class));
        IMI_STDOutput.deliver(IMI_STDInput.getData(StringInfo.class));
        STDOutput.deliver(STDInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
