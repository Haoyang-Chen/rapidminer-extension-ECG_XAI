package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
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
    private final InputPort LADInput = getSubprocess(0).getInnerSinks().createPort("LAD");
    private final OutputPort LADOutput=getOutputPorts().createPort("LAD");
    //LAFB
    private final InputPort LAFBInput = getSubprocess(0).getInnerSinks().createPort("LAFB");
    private final OutputPort LAFBOutput=getOutputPorts().createPort("LAFB");
    //RAD
    private final InputPort RADInput = getSubprocess(0).getInnerSinks().createPort("RAD");
    private final OutputPort RADOutput=getOutputPorts().createPort("RAD");
    //LPFB
    private final InputPort LPFBInput = getSubprocess(0).getInnerSinks().createPort("LPFB");
    private final OutputPort LPFBOutput=getOutputPorts().createPort("LPFB");


    public Step9Operator(OperatorDescription description) {
        super(description);
        getTransformer().addRule(new PassThroughRule(QRS_SUMOutput,QRS_SUMInput, false));
        getTransformer().addRule(new PassThroughRule(NORM_AXISInput, NORM_AXISOutput, false));
        getTransformer().addRule(new PassThroughRule(LADInput, LADOutput, false));
        getTransformer().addRule(new PassThroughRule(LAFBInput, LAFBOutput, false));
        getTransformer().addRule(new PassThroughRule(RADInput, RADOutput, false));
        getTransformer().addRule(new PassThroughRule(LPFBInput, LPFBOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        QRS_SUMInput.deliver(QRS_SUMOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        NORM_AXISOutput.deliver(NORM_AXISInput.getData(StringInfo.class));
        LADOutput.deliver(LADInput.getData(StringInfo.class));
        LAFBOutput.deliver(LAFBInput.getData(StringInfo.class));
        RADOutput.deliver(RADInput.getData(StringInfo.class));
        LPFBOutput.deliver(LPFBInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
