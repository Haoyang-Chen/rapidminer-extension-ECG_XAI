package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Pack;
import com.rapidminer.extension.ecg_xai.operator.StringInfo_General;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class Step1Operator extends AbstractStepOperator {
    private final InputPort SINUSOutput=getInputPorts().createPort("SINUS");
    private final OutputPort SINUSInput = getSubprocess(0).getInnerSources().createPort("SINUS");
    private final InputPort HROutput=getInputPorts().createPort("HR");
    private final OutputPort HRInput = getSubprocess(0).getInnerSources().createPort("HR");
    private final InputPort RR_DIFFOutput=getInputPorts().createPort("RR_DIFF");
    private final OutputPort RR_DIFFInput = getSubprocess(0).getInnerSources().createPort("RR_DIFF");
    //SARRH
    private final InputPort SARRHInput = getSubprocess(0).getInnerSinks().createPort("SARRH");
    private final OutputPort SARRHOutput=getOutputPorts().createPort("SARRH");
    //STACH
    private final InputPort STACHInput = getSubprocess(0).getInnerSinks().createPort("STACH");
    private final OutputPort STACHOutput=getOutputPorts().createPort("STACH");
    //SR
    private final InputPort SRInput = getSubprocess(0).getInnerSinks().createPort("SR");
    private final OutputPort SROutput=getOutputPorts().createPort("SR");
    //SBRAD
    private final InputPort SBRADInput = getSubprocess(0).getInnerSinks().createPort("SBRAD");
    private final OutputPort SBRADOutput=getOutputPorts().createPort("SBRAD");
    //AFIB
    private final InputPort AFIBInput = getSubprocess(0).getInnerSinks().createPort("AFIB");
    private final OutputPort AFIBOutput=getOutputPorts().createPort("AFIB");
    //AFLT
    private final InputPort AFLTInput = getSubprocess(0).getInnerSinks().createPort("AFLT");
    private final OutputPort AFLTOutput=getOutputPorts().createPort("AFLT");


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
        SINUSInput.deliver(SINUSOutput.getData(StringInfo_General.class));
        HRInput.deliver(HROutput.getData(StringInfo_General.class));
        RR_DIFFInput.deliver(RR_DIFFOutput.getData(StringInfo_General.class));
        getSubprocess(0).execute();
        OutModelOutput.deliver(InModelInput.getData(Pack.class));
        SARRHOutput.deliver(SARRHInput.getData(StringInfo_General.class));
        STACHOutput.deliver(STACHInput.getData(StringInfo_General.class));
        SROutput.deliver(SRInput.getData(StringInfo_General.class));
        SBRADOutput.deliver(SBRADInput.getData(StringInfo_General.class));
        AFIBOutput.deliver(AFIBInput.getData(StringInfo_General.class));
        AFLTOutput.deliver(AFLTInput.getData(StringInfo_General.class));
        outExtender.collect();
    }
}
