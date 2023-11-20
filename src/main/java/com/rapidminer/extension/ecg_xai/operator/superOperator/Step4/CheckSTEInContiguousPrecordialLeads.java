package com.rapidminer.extension.ecg_xai.operator.superOperator.Step4;

        import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
        import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
        import com.rapidminer.operator.OperatorChain;
        import com.rapidminer.operator.OperatorDescription;
        import com.rapidminer.operator.OperatorException;
        import com.rapidminer.operator.ports.InputPort;
        import com.rapidminer.operator.ports.OutputPort;
        import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class CheckSTEInContiguousPrecordialLeads extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    private final InputPort ST_AMPOutput=getInputPorts().createPort("ST_AMP ST amplitude");
    private final OutputPort ST_AMPInput = getSubprocess(0).getInnerSources().createPort("ST_AMP ST amplitude");
    private final InputPort AMI_STEInput = getSubprocess(0).getInnerSinks().createPort("AMI_STE acute anterior myocardial infarction");
    private final OutputPort AMI_STEOutput=getOutputPorts().createPort("AMI_STE acute anterior myocardial infarction");
    private final InputPort STEInput = getSubprocess(0).getInnerSinks().createPort("STE ST segment elevation");
    private final OutputPort STEOutput=getOutputPorts().createPort("STE ST segment elevation");

    public CheckSTEInContiguousPrecordialLeads(OperatorDescription description) {
        super(description,"Executed Process");
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(ST_AMPOutput,ST_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(AMI_STEInput, AMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(STEInput, STEOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        ST_AMPInput.deliver(ST_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        AMI_STEOutput.deliver(AMI_STEInput.getData(Pack.class));
        STEOutput.deliver(STEInput.getData(StringInfo.class));
    }
}
