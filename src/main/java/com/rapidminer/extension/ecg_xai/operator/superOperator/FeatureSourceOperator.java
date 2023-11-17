package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.StringInfo_General;
import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.CollectingPortPairExtender;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;


public class FeatureSourceOperator extends OperatorChain {
    private final OutputPort SINUSOutput=getOutputPorts().createPort("SINUS");
    private final InputPort SINUSInput = getSubprocess(0).getInnerSinks().createPort("SINUS");
    private final OutputPort HROutput=getOutputPorts().createPort("HR");
    private final InputPort HRInput = getSubprocess(0).getInnerSinks().createPort("HR");
    private final OutputPort RR_DIFFOutput=getOutputPorts().createPort("RR_DIFF");
    private final InputPort RR_DIFFInput = getSubprocess(0).getInnerSinks().createPort("RR_DIFF");
    private final OutputPort QRS_DUROutput=getOutputPorts().createPort("QRS_DUR");
    private final InputPort QRS_DURInput = getSubprocess(0).getInnerSinks().createPort("QRS_DUR");
    private final OutputPort PR_DUROutput=getOutputPorts().createPort("PR_DUR");
    private final InputPort PR_DURInput = getSubprocess(0).getInnerSinks().createPort("PR_DUR");
    private final OutputPort ST_AMPOutput=getOutputPorts().createPort("ST_AMP");
    private final InputPort ST_AMPInput = getSubprocess(0).getInnerSinks().createPort("ST_AMP");
    private final OutputPort Q_DUROutput=getOutputPorts().createPort("Q_DUR");
    private final InputPort Q_DURInput = getSubprocess(0).getInnerSinks().createPort("Q_DUR");
    private final OutputPort Q_AMPOutput=getOutputPorts().createPort("Q_AMP");
    private final InputPort Q_AMPInput = getSubprocess(0).getInnerSinks().createPort("Q_AMP");
    private final OutputPort PRWPOutput=getOutputPorts().createPort("PRWP");
    private final InputPort PRWPInput = getSubprocess(0).getInnerSinks().createPort("PRWP");
    private final OutputPort P_DUROutput=getOutputPorts().createPort("P_DUR");
    private final InputPort P_DURInput = getSubprocess(0).getInnerSinks().createPort("P_DUR");
    private final OutputPort P_AMPOutput=getOutputPorts().createPort("P_AMP");
    private final InputPort P_AMPInput = getSubprocess(0).getInnerSinks().createPort("P_AMP");
    private final OutputPort AGEOutput=getOutputPorts().createPort("AGE");
    private final InputPort AGEInput = getSubprocess(0).getInnerSinks().createPort("AGE");
    private final OutputPort MALEOutput=getOutputPorts().createPort("MALE");
    private final InputPort MALEInput = getSubprocess(0).getInnerSinks().createPort("MALE");
    private final OutputPort R_AMPOutput=getOutputPorts().createPort("R_AMP");
    private final InputPort R_AMPInput = getSubprocess(0).getInnerSinks().createPort("R_AMP");
    private final OutputPort S_AMPOutput=getOutputPorts().createPort("S_AMP");
    private final InputPort S_AMPInput = getSubprocess(0).getInnerSinks().createPort("S_AMP");
    private final OutputPort RS_RATIOOutput=getOutputPorts().createPort("RS_RATIO");
    private final InputPort RS_RATIOInput = getSubprocess(0).getInnerSinks().createPort("RS_RATIO");
    private final OutputPort RADOutput=getOutputPorts().createPort("RAD");
    private final InputPort RADInput = getSubprocess(0).getInnerSinks().createPort("RAD");
    private final OutputPort T_AMPOutput=getOutputPorts().createPort("T_AMP");
    private final InputPort T_AMPInput = getSubprocess(0).getInnerSinks().createPort("T_AMP");
    private final OutputPort QRS_SUMOutput=getOutputPorts().createPort("QRS_SUM");
    private final InputPort QRS_SUMInput = getSubprocess(0).getInnerSinks().createPort("QRS_SUM");

    public FeatureSourceOperator(OperatorDescription description) {
        super(description, "Executed Process");
        outExtender.start();
        getTransformer().addRule(new PassThroughRule(SINUSInput, SINUSOutput, false));
        getTransformer().addRule(new PassThroughRule(HRInput, HROutput, false));
        getTransformer().addRule(new PassThroughRule(RR_DIFFInput, RR_DIFFOutput, false));
        getTransformer().addRule(new PassThroughRule(QRS_DURInput, QRS_DUROutput, false));
        getTransformer().addRule(new PassThroughRule(PR_DURInput, PR_DUROutput, false));
        getTransformer().addRule(new PassThroughRule(ST_AMPInput, ST_AMPOutput, false));
        getTransformer().addRule(new PassThroughRule(Q_DURInput, Q_DUROutput, false));
        getTransformer().addRule(new PassThroughRule(Q_AMPInput, Q_AMPOutput, false));
        getTransformer().addRule(new PassThroughRule(PRWPInput, PRWPOutput, false));
        getTransformer().addRule(new PassThroughRule(P_DURInput, P_DUROutput, false));
        getTransformer().addRule(new PassThroughRule(P_AMPInput, P_AMPOutput, false));
        getTransformer().addRule(new PassThroughRule(AGEInput, AGEOutput, false));
        getTransformer().addRule(new PassThroughRule(MALEInput, MALEOutput, false));
        getTransformer().addRule(new PassThroughRule(R_AMPInput, R_AMPOutput, false));
        getTransformer().addRule(new PassThroughRule(S_AMPInput, S_AMPOutput, false));
        getTransformer().addRule(new PassThroughRule(RS_RATIOInput, RS_RATIOOutput, false));
        getTransformer().addRule(new PassThroughRule(RADInput, RADOutput, false));
        getTransformer().addRule(new PassThroughRule(T_AMPInput, T_AMPOutput, false));
        getTransformer().addRule(new PassThroughRule(QRS_SUMInput, QRS_SUMOutput, false));
        getTransformer().addRule(outExtender.makePassThroughRule());
    }
    private final CollectingPortPairExtender outExtender =
            new CollectingPortPairExtender("other",
                    getSubprocess(0).getInnerSinks(), getOutputPorts());


    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        getSubprocess(0).execute();
        if(SINUSInput.isConnected()) {
            SINUSOutput.deliver(SINUSInput.getData(StringInfo_General.class));
        }
        if(HRInput.isConnected()) {
            HROutput.deliver(HRInput.getData(StringInfo_General.class));
        }
        if(RR_DIFFInput.isConnected()) {
            RR_DIFFOutput.deliver(RR_DIFFInput.getData(StringInfo_General.class));
        }
        if(QRS_DURInput.isConnected()) {
            QRS_DUROutput.deliver(QRS_DURInput.getData(StringInfo_General.class));
        }
        if(PR_DURInput.isConnected()) {
            PR_DUROutput.deliver(PR_DURInput.getData(StringInfo_General.class));
        }
        if(ST_AMPInput.isConnected()) {
            ST_AMPOutput.deliver(ST_AMPInput.getData(StringInfo_General.class));
        }
        if(Q_DURInput.isConnected()) {
            Q_DUROutput.deliver(Q_DURInput.getData(StringInfo_General.class));
        }
        if(Q_AMPInput.isConnected()) {
            Q_AMPOutput.deliver(Q_AMPInput.getData(StringInfo_General.class));
        }
        if(PRWPInput.isConnected()) {
            PRWPOutput.deliver(PRWPInput.getData(StringInfo_General.class));
        }
        if(P_DURInput.isConnected()) {
            P_DUROutput.deliver(P_DURInput.getData(StringInfo_General.class));
        }
        if(P_AMPInput.isConnected()) {
            P_AMPOutput.deliver(P_AMPInput.getData(StringInfo_General.class));
        }
        if(AGEInput.isConnected()) {
            AGEOutput.deliver(AGEInput.getData(StringInfo_General.class));
        }
        if(MALEInput.isConnected()) {
            MALEOutput.deliver(MALEInput.getData(StringInfo_General.class));
        }
        if(R_AMPInput.isConnected()) {
            R_AMPOutput.deliver(R_AMPInput.getData(StringInfo_General.class));
        }
        if(S_AMPInput.isConnected()) {
            S_AMPOutput.deliver(S_AMPInput.getData(StringInfo_General.class));
        }
        if(RS_RATIOInput.isConnected()) {
            RS_RATIOOutput.deliver(RS_RATIOInput.getData(StringInfo_General.class));
        }
        if(RADInput.isConnected()) {
            RADOutput.deliver(RADInput.getData(StringInfo_General.class));
        }
        if(T_AMPInput.isConnected()) {
            T_AMPOutput.deliver(T_AMPInput.getData(StringInfo_General.class));
        }
        if(QRS_SUMInput.isConnected()) {
            QRS_SUMOutput.deliver(QRS_SUMInput.getData(StringInfo_General.class));
        }
        outExtender.collect();
    }

}
