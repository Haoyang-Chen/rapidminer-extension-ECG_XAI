package com.rapidminer.extension.ecg_xai.operator.superOperator.Step3;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step3Operator extends AbstractStepOperator {
    private final InputPort QRS_DUROutput=getInputPorts().createPort("QRS_DUR QRS duration");
    private final OutputPort QRS_DURInput = getSubprocess(0).getInnerSources().createPort("QRS_DUR QRS duration");
    private final InputPort PR_DUROutput=getInputPorts().createPort("PR_DUR PR duration");
    private final OutputPort PR_DURInput = getSubprocess(0).getInnerSources().createPort("PR_DUR PR duration");
    private final InputPort LBBBOutput=getInputPorts().createPort("LBBB left bundle branch block");
    private final OutputPort LBBBInput = getSubprocess(0).getInnerSources().createPort("LBBB left bundle branch block");
    private final InputPort RBBBOutput=getInputPorts().createPort("RBBB right bundle branch block");
    private final OutputPort RBBBInput = getSubprocess(0).getInnerSources().createPort("RBBB right bundle branch block");

    //WPW
    private final InputPort WPWInput = getSubprocess(0).getInnerSinks().createPort("WPW Wolff-Parkinson-White syndrome");
    private final OutputPort WPWOutput=getOutputPorts().createPort("WPW Wolff-Parkinson-White syndrome");
    //IVCD
    private final InputPort IVCDInput = getSubprocess(0).getInnerSinks().createPort("IVCD intraventricular conduction delay");
    private final OutputPort IVCDOutput=getOutputPorts().createPort("IVCD intraventricular conduction delay");


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
        Pack pack=new Pack();
        InControlOutput.deliver(pack);
        QRS_DURInput.deliver(QRS_DUROutput.getData(StringInfo.class));
        PR_DURInput.deliver(PR_DUROutput.getData(StringInfo.class));
        LBBBInput.deliver(LBBBOutput.getData(StringInfo.class));
        RBBBInput.deliver(RBBBOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
//        OutControlOutput.deliver(InControlInput.getData(Pack.class));
        OutSummaryOutput.deliver(pack);
        WPWOutput.deliver(WPWInput.getData(StringInfo.class));
        IVCDOutput.deliver(IVCDInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
