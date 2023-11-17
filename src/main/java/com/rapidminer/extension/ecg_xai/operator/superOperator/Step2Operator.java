package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo_General;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step2Operator extends AbstractStepOperator {
    private final InputPort QRS_DUROutput=getInputPorts().createPort("QRS_DUR");
    private final OutputPort QRS_DURInput = getSubprocess(0).getInnerSources().createPort("QRS_DUR");
    private final InputPort PR_DUROutput=getInputPorts().createPort("PR_DUR");
    private final OutputPort PR_DURInput = getSubprocess(0).getInnerSources().createPort("PR_DUR");
    //SARRH
    private final InputPort LBBBInput = getSubprocess(0).getInnerSinks().createPort("LBBB");
    private final OutputPort LBBBOutput=getOutputPorts().createPort("LBBB");
    //STACH
    private final InputPort RBBBInput = getSubprocess(0).getInnerSinks().createPort("RBBB");
    private final OutputPort RBBBOutput=getOutputPorts().createPort("RBBB");
    //SR
    private final InputPort AVBInput = getSubprocess(0).getInnerSinks().createPort("AVB");
    private final OutputPort AVBOutput=getOutputPorts().createPort("AVB");


    public Step2Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(QRS_DUROutput,QRS_DURInput, false));
        getTransformer().addRule(new PassThroughRule(PR_DUROutput, PR_DURInput, false));
        getTransformer().addRule(new PassThroughRule(LBBBInput, LBBBOutput, false));
        getTransformer().addRule(new PassThroughRule(RBBBInput, RBBBOutput, false));
        getTransformer().addRule(new PassThroughRule(AVBInput, AVBOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        QRS_DURInput.deliver(QRS_DUROutput.getData(StringInfo_General.class));
        PR_DURInput.deliver(PR_DUROutput.getData(StringInfo_General.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        LBBBOutput.deliver(LBBBInput.getData(StringInfo_General.class));
        RBBBOutput.deliver(RBBBInput.getData(StringInfo_General.class));
        AVBOutput.deliver(AVBInput.getData(StringInfo_General.class));
        outExtender.collect();
    }
}
