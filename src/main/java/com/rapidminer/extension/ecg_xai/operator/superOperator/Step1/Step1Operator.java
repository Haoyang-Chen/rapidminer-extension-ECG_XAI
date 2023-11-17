package com.rapidminer.extension.ecg_xai.operator.superOperator.Step1;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step1Operator extends AbstractStepOperator {
    private final InputPort SINUSOutput=getInputPorts().createPort("SINUS rhythm is sinus");
    private final OutputPort SINUSInput = getSubprocess(0).getInnerSources().createPort("SINUS rhythm is sinus");
    private final InputPort HROutput=getInputPorts().createPort("HR heart rate");
    private final OutputPort HRInput = getSubprocess(0).getInnerSources().createPort("HR heart rate");
    private final InputPort RR_DIFFOutput=getInputPorts().createPort("RR_DIFF RR interval");
    private final OutputPort RR_DIFFInput = getSubprocess(0).getInnerSources().createPort("RR_DIFF RR interval");
    //SARRH
    private final InputPort SARRHInput = getSubprocess(0).getInnerSinks().createPort("SARRH sinus arrhythmia");
    private final OutputPort SARRHOutput=getOutputPorts().createPort("SARRH sinus arrhythmia");
    //STACH
    private final InputPort STACHInput = getSubprocess(0).getInnerSinks().createPort("STACH sinus tachycardia");
    private final OutputPort STACHOutput=getOutputPorts().createPort("STACH sinus tachycardia");
    //SR
    private final InputPort SRInput = getSubprocess(0).getInnerSinks().createPort("SR sinus rhythm");
    private final OutputPort SROutput=getOutputPorts().createPort("SR sinus rhythm");
    //SBRAD
    private final InputPort SBRADInput = getSubprocess(0).getInnerSinks().createPort("SBRAD sinus bradycardia");
    private final OutputPort SBRADOutput=getOutputPorts().createPort("SBRAD sinus bradycardia");
    //AFIB
    private final InputPort AFIBInput = getSubprocess(0).getInnerSinks().createPort("AFIB atrial fibrillation");
    private final OutputPort AFIBOutput=getOutputPorts().createPort("AFIB atrial fibrillation");
    //AFLT
    private final InputPort AFLTInput = getSubprocess(0).getInnerSinks().createPort("AFLT atrial flutter");
    private final OutputPort AFLTOutput=getOutputPorts().createPort("AFLT atrial flutter");


    public Step1Operator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(SINUSOutput,SINUSInput, false));
        getTransformer().addRule(new PassThroughRule(HROutput, HRInput, false));
        getTransformer().addRule(new PassThroughRule(RR_DIFFOutput, RR_DIFFInput, false));
        getTransformer().addRule(new PassThroughRule(SARRHInput, SARRHOutput, false));
        getTransformer().addRule(new PassThroughRule(STACHInput, STACHOutput, false));
        getTransformer().addRule(new PassThroughRule(SRInput, SROutput, false));
        getTransformer().addRule(new PassThroughRule(SBRADInput, SBRADOutput, false));
        getTransformer().addRule(new PassThroughRule(AFIBInput, AFIBOutput, false));
        getTransformer().addRule(new PassThroughRule(AFLTInput, AFLTOutput, false));
        getTransformer().addRule(inExtender.makePassThroughRule());
        getTransformer().addRule(outExtender.makePassThroughRule());
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        SINUSInput.deliver(SINUSOutput.getData(StringInfo.class));
        HRInput.deliver(HROutput.getData(StringInfo.class));
        RR_DIFFInput.deliver(RR_DIFFOutput.getData(StringInfo.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        SARRHOutput.deliver(SARRHInput.getData(StringInfo.class));
        STACHOutput.deliver(STACHInput.getData(StringInfo.class));
        SROutput.deliver(SRInput.getData(StringInfo.class));
        SBRADOutput.deliver(SBRADInput.getData(StringInfo.class));
        AFIBOutput.deliver(AFIBInput.getData(StringInfo.class));
        AFLTOutput.deliver(AFLTInput.getData(StringInfo.class));
        outExtender.collect();
    }
}
