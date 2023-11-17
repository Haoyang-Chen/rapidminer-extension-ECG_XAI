package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo_General;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step3Operator extends AbstractStepOperator {
    private final InputPort QRS_DUROutput=getInputPorts().createPort("QRS_DUR");
    private final OutputPort QRS_DURInput = getSubprocess(0).getInnerSources().createPort("QRS_DUR");
    private final InputPort PR_DUROutput=getInputPorts().createPort("PR_DUR");
    private final OutputPort PR_DURInput = getSubprocess(0).getInnerSources().createPort("PR_DUR");
    private final InputPort LBBBOutput=getInputPorts().createPort("LBBB");
    private final OutputPort LBBBInput = getSubprocess(0).getInnerSources().createPort("LBBB");
    private final InputPort RBBBOutput=getInputPorts().createPort("RBBB");
    private final OutputPort RBBBInput = getSubprocess(0).getInnerSources().createPort("RBBB");

    //WPW
    private final InputPort WPWInput = getSubprocess(0).getInnerSinks().createPort("WPW");
    private final OutputPort WPWOutput=getOutputPorts().createPort("WPW");
    //IVCD
    private final InputPort IVCDInput = getSubprocess(0).getInnerSinks().createPort("IVCD");
    private final OutputPort IVCDOutput=getOutputPorts().createPort("IVCD");


    public Step3Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(QRS_DUROutput,QRS_DURInput, false));
        getTransformer().addRule(new PassThroughRule(PR_DUROutput, PR_DURInput, false));
        getTransformer().addRule(new PassThroughRule(LBBBOutput, LBBBInput, false));
        getTransformer().addRule(new PassThroughRule(RBBBOutput, RBBBInput, false));
        getTransformer().addRule(new PassThroughRule(WPWInput, WPWOutput, false));
        getTransformer().addRule(new PassThroughRule(IVCDInput, IVCDOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        QRS_DURInput.deliver(QRS_DUROutput.getData(StringInfo_General.class));
        PR_DURInput.deliver(PR_DUROutput.getData(StringInfo_General.class));
        LBBBInput.deliver(LBBBOutput.getData(StringInfo_General.class));
        RBBBInput.deliver(RBBBOutput.getData(StringInfo_General.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        WPWOutput.deliver(WPWInput.getData(StringInfo_General.class));
        IVCDOutput.deliver(IVCDInput.getData(StringInfo_General.class));
        outExtender.collect();
    }
}
