package com.rapidminer.extension.ecg_xai.operator.superOperator.Step4;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;

public class CheckSTDInInferiorLeads extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    private final InputPort ST_AMPOutput=getInputPorts().createPort("ST_AMP ST amplitude");
    private final OutputPort ST_AMPInput = getSubprocess(0).getInnerSources().createPort("ST_AMP ST amplitude");

    private final InputPort AMI_STDInput = getSubprocess(0).getInnerSinks().createPort("AMI_STD acute anterior myocardial infarction");
    private final OutputPort AMI_STDOutput=getOutputPorts().createPort("AMI_STD acute anterior myocardial infarction");
    //LMI_STD
    private final InputPort LMI_STDInput = getSubprocess(0).getInnerSinks().createPort("LMI_STD acute lateral myocardial infarction");
    private final OutputPort LMI_STDOutput=getOutputPorts().createPort("LMI_STD acute lateral myocardial infarction");
    private final InputPort STDInput = getSubprocess(0).getInnerSinks().createPort("STD ST segment depression");
    private final OutputPort STDOutput=getOutputPorts().createPort("STD ST segment depression");

    public CheckSTDInInferiorLeads(OperatorDescription description) {
        super(description,"Executed Process");
        getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(ST_AMPOutput,ST_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(AMI_STDInput, AMI_STDOutput, false));
        getTransformer().addRule(new PassThroughRule(LMI_STDInput, LMI_STDOutput, false));
        getTransformer().addRule(new PassThroughRule(STDInput, STDOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        ST_AMPInput.deliver(ST_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        AMI_STDOutput.deliver(AMI_STDInput.getData(Pack.class));
        LMI_STDOutput.deliver(LMI_STDInput.getData(Pack.class));
        STDOutput.deliver(STDInput.getData(StringInfo.class));
    }
}
