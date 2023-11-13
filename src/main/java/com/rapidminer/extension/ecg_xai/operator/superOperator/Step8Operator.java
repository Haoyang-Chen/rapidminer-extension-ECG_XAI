package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step8Operator extends AbstractStepOperator {
    private final InputPort T_AMPOutput=getInputPorts().createPort("T_AMP");
    private final OutputPort T_AMPInput = getSubprocess(0).getInnerSources().createPort("T_AMP");
    private final InputPort STEOutput=getInputPorts().createPort("STE");
    private final OutputPort STEInput = getSubprocess(0).getInnerSources().createPort("STE");
    private final InputPort STDOutput=getInputPorts().createPort("STD");
    private final OutputPort STDInput = getSubprocess(0).getInnerSources().createPort("STD");

    //IMI
    private final InputPort IMIInput = getSubprocess(0).getInnerSinks().createPort("IMI");
    private final OutputPort IMIOutput=getOutputPorts().createPort("IMI");
    //AMI
    private final InputPort AMIInput = getSubprocess(0).getInnerSinks().createPort("AMI");
    private final OutputPort AMIOutput=getOutputPorts().createPort("AMI");
    //LMI
    private final InputPort LMIInput = getSubprocess(0).getInnerSinks().createPort("LMI");
    private final OutputPort LMIOutput=getOutputPorts().createPort("LMI");
    //LVH
    private final InputPort LVHInput = getSubprocess(0).getInnerSinks().createPort("LVH");
    private final OutputPort LVHOutput=getOutputPorts().createPort("LVH");
    //RVH
    private final InputPort RVHInput = getSubprocess(0).getInnerSinks().createPort("RVH");
    private final OutputPort RVHOutput=getOutputPorts().createPort("RVH");


    public Step8Operator(OperatorDescription description) {
        super(description);
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
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        T_AMPInput.deliver(T_AMPOutput.getData(StringInfo.class));
        STEInput.deliver(STEOutput.getData(StringInfo.class));
        STDInput.deliver(STDOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        IMIOutput.deliver(IMIInput.getData(StringInfo.class));
        AMIOutput.deliver(AMIInput.getData(StringInfo.class));
        LMIOutput.deliver(LMIInput.getData(StringInfo.class));
        LVHOutput.deliver(LVHInput.getData(StringInfo.class));
        RVHOutput.deliver(RVHInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
