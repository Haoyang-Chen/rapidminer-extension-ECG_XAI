package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo_General;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step4Operator extends AbstractStepOperator {
    private final InputPort ST_AMPOutput=getInputPorts().createPort("ST_AMP");
    private final OutputPort ST_AMPInput = getSubprocess(0).getInnerSources().createPort("ST_AMP");

    private final InputPort LMI_STEInput = getSubprocess(0).getInnerSinks().createPort("LMI_STE");
    private final OutputPort LMI_STEOutput=getOutputPorts().createPort("LMI_STE");
    private final InputPort AMI_STEInput = getSubprocess(0).getInnerSinks().createPort("AMI_STE");
    private final OutputPort AMI_STEOutput=getOutputPorts().createPort("AMI_STE");
    //IMI
    private final InputPort IMI_STEInput = getSubprocess(0).getInnerSinks().createPort("IMI_STE");
    private final OutputPort IMI_STEOutput=getOutputPorts().createPort("IMI_STE");
    //STE
    private final InputPort STEInput = getSubprocess(0).getInnerSinks().createPort("STE");
    private final OutputPort STEOutput=getOutputPorts().createPort("STE");
    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH");
    //AMI_STD
    private final InputPort AMI_STDInput = getSubprocess(0).getInnerSinks().createPort("AMI_STD");
    private final OutputPort AMI_STDOutput=getOutputPorts().createPort("AMI_STD");
    //LMI_STD
    private final InputPort LMI_STDInput = getSubprocess(0).getInnerSinks().createPort("LMI_STD");
    private final OutputPort LMI_STDOutput=getOutputPorts().createPort("LMI_STD");
    //IMI_STD
    private final InputPort IMI_STDInput = getSubprocess(0).getInnerSinks().createPort("IMI_STD");
    private final OutputPort IMI_STDOutput=getOutputPorts().createPort("IMI_STD");
    //STD
    private final InputPort STDInput = getSubprocess(0).getInnerSinks().createPort("STD");
    private final OutputPort STDOutput=getOutputPorts().createPort("STD");


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
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        ST_AMPInput.deliver(ST_AMPOutput.getData(StringInfo_General.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        LMI_STEOutput.deliver(LMI_STEInput.getData(StringInfo_General.class));
        AMI_STEOutput.deliver(AMI_STEInput.getData(StringInfo_General.class));
        IMI_STEOutput.deliver(IMI_STEInput.getData(StringInfo_General.class));
        STEOutput.deliver(STEInput.getData(StringInfo_General.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo_General.class));
        RVHOutput.deliver(RVHInput.getData(StringInfo_General.class));
        AMI_STDOutput.deliver(AMI_STDInput.getData(StringInfo_General.class));
        LMI_STDOutput.deliver(LMI_STDInput.getData(StringInfo_General.class));
        IMI_STDOutput.deliver(IMI_STDInput.getData(StringInfo_General.class));
        STDOutput.deliver(STDInput.getData(StringInfo_General.class));
        outExtender.collect();
    }
}
