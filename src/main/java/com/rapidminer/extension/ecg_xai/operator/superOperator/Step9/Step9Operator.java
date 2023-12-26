package com.rapidminer.extension.ecg_xai.operator.superOperator.Step9;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step9Operator extends AbstractStepOperator {
    private final InputPort QRS_SUMOutput=getInputPorts().createPort("QRS_SUM");
    private final OutputPort QRS_SUMInput = getSubprocess(0).getInnerSources().createPort("QRS_SUM");

    //NORM_AXIS
    private final InputPort NORM_AXISInput = getSubprocess(0).getInnerSinks().createPort("NORM_AXIS");
    private final OutputPort NORM_AXISOutput=getOutputPorts().createPort("NORM_AXIS");
    //LAD
    private final InputPort LADInput = getSubprocess(0).getInnerSinks().createPort("LAD left axis deviation");
    private final OutputPort LADOutput=getOutputPorts().createPort("LAD left axis deviation");
    //LAFB
    private final InputPort LAFBInput = getSubprocess(0).getInnerSinks().createPort("LAFB left anterior fascicular block");
    private final OutputPort LAFBOutput=getOutputPorts().createPort("LAFB left anterior fascicular block");
    //RAD
    private final InputPort RADInput = getSubprocess(0).getInnerSinks().createPort("RAD right axis deviation");
    private final OutputPort RADOutput=getOutputPorts().createPort("RAD right axis deviation");
    //LPFB
    private final InputPort LPFBInput = getSubprocess(0).getInnerSinks().createPort("LPFB left posterior fascicular block");
    private final OutputPort LPFBOutput=getOutputPorts().createPort("LPFB left posterior fascicular block");


    public Step9Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(QRS_SUMOutput,QRS_SUMInput, false));
        getTransformer().addRule(new PassThroughRule(NORM_AXISInput, NORM_AXISOutput, false));
        getTransformer().addRule(new PassThroughRule(LADInput, LADOutput, false));
        getTransformer().addRule(new PassThroughRule(LAFBInput, LAFBOutput, false));
        getTransformer().addRule(new PassThroughRule(RADInput, RADOutput, false));
        getTransformer().addRule(new PassThroughRule(LPFBInput, LPFBOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        QRS_SUMInput.deliver(QRS_SUMOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutSummaryOutput.deliver(getSubprocess(0).getAllInnerOperators().get(0).getOutputPorts().getPortByIndex(0).getData(Pack.class));
        NORM_AXISOutput.deliver(NORM_AXISInput.getData(StringInfo.class));
        LADOutput.deliver(LADInput.getData(StringInfo.class));
        LAFBOutput.deliver(LAFBInput.getData(StringInfo.class));
        RADOutput.deliver(RADInput.getData(StringInfo.class));
        LPFBOutput.deliver(LPFBInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
