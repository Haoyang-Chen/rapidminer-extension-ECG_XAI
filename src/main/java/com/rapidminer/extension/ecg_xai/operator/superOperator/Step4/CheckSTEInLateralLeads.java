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

public class CheckSTEInLateralLeads extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    private final InputPort ST_AMPOutput=getInputPorts().createPort("ST_AMP ST amplitude");
    private final OutputPort ST_AMPInput = getSubprocess(0).getInnerSources().createPort("ST_AMP ST amplitude");

    private final InputPort LMI_STEInput = getSubprocess(0).getInnerSinks().createPort("LMI_STE acute lateral myocardial infarction");
    private final OutputPort LMI_STEOutput=getOutputPorts().createPort("LMI_STE acute lateral myocardial infarction");
    private final InputPort STEInput = getSubprocess(0).getInnerSinks().createPort("STE ST segment elevation");
    private final OutputPort STEOutput=getOutputPorts().createPort("STE ST segment elevation");

    public CheckSTEInLateralLeads(OperatorDescription description) {
        super(description,"Executed Process");
        getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(ST_AMPOutput,ST_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(LMI_STEInput, LMI_STEOutput, false));
        getTransformer().addRule(new PassThroughRule(STEInput, STEOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        ST_AMPInput.deliver(ST_AMPOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        LMI_STEOutput.deliver(LMI_STEInput.getData(Pack.class));
        STEOutput.deliver(STEInput.getData(StringInfo.class));
    }
}
