package com.rapidminer.extension.ecg_xai.operator.superOperator.Step8;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step8Operator extends AbstractStepOperator {
    private final InputPort T_AMPOutput=getInputPorts().createPort("T_AMP T wave amplitude");
    private final OutputPort T_AMPInput = getSubprocess(0).getInnerSources().createPort("T_AMP T wave amplitude");
    private final InputPort STEOutput=getInputPorts().createPort("STE ST elevation");
    private final OutputPort STEInput = getSubprocess(0).getInnerSources().createPort("STE ST elevation");
    private final InputPort STDOutput=getInputPorts().createPort("STD ST depression");
    private final OutputPort STDInput = getSubprocess(0).getInnerSources().createPort("STD ST depression");

    //IMI
    private final InputPort IMIInput = getSubprocess(0).getInnerSinks().createPort("IMI acute inferior myocardial infarction");
    private final OutputPort IMIOutput=getOutputPorts().createPort("IMI acute inferior myocardial infarction");
    //AMI
    private final InputPort AMIInput = getSubprocess(0).getInnerSinks().createPort("AMI acute anterior myocardial infarction");
    private final OutputPort AMIOutput=getOutputPorts().createPort("AMI acute anterior myocardial infarction");
    //LMI
    private final InputPort LMIInput = getSubprocess(0).getInnerSinks().createPort("LMI acute lateral myocardial infarction");
    private final OutputPort LMIOutput=getOutputPorts().createPort("LMI acute lateral myocardial infarction");
    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH left ventricular hypertrophy");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH left ventricular hypertrophy");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH right ventricular hypertrophy");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH right ventricular hypertrophy");


    public Step8Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(T_AMPOutput,T_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(STEOutput, STEInput, false));
        getTransformer().addRule(new PassThroughRule(STDOutput, STDInput, false));
        getTransformer().addRule(new PassThroughRule(IMIInput, IMIOutput, false));
        getTransformer().addRule(new PassThroughRule(AMIInput, AMIOutput, false));
        getTransformer().addRule(new PassThroughRule(LMIInput, LMIOutput, false));
        getTransformer().addRule(new PassThroughRule(LVHInput, LVHOutput, false));
        getTransformer().addRule(new PassThroughRule(RVHInput, RVHOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        T_AMPInput.deliver(T_AMPOutput.getData(StringInfo.class));
        STEInput.deliver(STEOutput.getData(StringInfo.class));
        STDInput.deliver(STDOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutSummaryOutput.deliver(getSubprocess(0).getAllInnerOperators().get(0).getOutputPorts().getPortByIndex(0).getData(Pack.class));
        IMIOutput.deliver(IMIInput.getData(StringInfo.class));
        AMIOutput.deliver(AMIInput.getData(StringInfo.class));
        LMIOutput.deliver(LMIInput.getData(StringInfo.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo.class));
        RVHOutput.deliver(RVHInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
