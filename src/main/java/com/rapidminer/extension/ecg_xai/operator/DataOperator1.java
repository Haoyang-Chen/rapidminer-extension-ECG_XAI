package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.OutputPort;

public class DataOperator1 extends Operator {
    private final OutputPort HROutput=getOutputPorts().createPort("HR");
    private final OutputPort RR_DIFFOutput=getOutputPorts().createPort("RR_DIFF");
    private final OutputPort PR_DUROutput=getOutputPorts().createPort("PR_DUR");
    private final OutputPort QRS_DUROutput=getOutputPorts().createPort("QRS_DUR");
    private final OutputPort ST_AMPOutput=getOutputPorts().createPort("ST_AMP");
    private final OutputPort Q_DUROutput=getOutputPorts().createPort("Q_DUR");
    private final OutputPort Q_AMPOutput=getOutputPorts().createPort("Q_AMP");
    private final OutputPort P_DUROutput=getOutputPorts().createPort("P_DUR");
    private final OutputPort P_AMPOutput=getOutputPorts().createPort("P_AMP");
    private final OutputPort AGEOutput=getOutputPorts().createPort("AGE");
    private final OutputPort MALEOutput=getOutputPorts().createPort("MALE");
    private final OutputPort R_AMPOutput=getOutputPorts().createPort("R_AMP");
    private final OutputPort S_AMPOutput=getOutputPorts().createPort("S_AMP");
    private final OutputPort RS_RATIOOutput=getOutputPorts().createPort("RS_RATIO");
    private final OutputPort T_AMPOutput=getOutputPorts().createPort("T_AMP");
    private final OutputPort QRS_SUMOutput=getOutputPorts().createPort("QRS_SUM");

    public DataOperator1(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        HROutput.deliver(new StringInfo("HR"));
        RR_DIFFOutput.deliver(new StringInfo("RR_DIFF"));
        PR_DUROutput.deliver(new StringInfo("PR_DUR"));
        QRS_DUROutput.deliver(new StringInfo("QRS_DUR"));
        ST_AMPOutput.deliver(new StringInfo("ST_AMP"));
        Q_DUROutput.deliver(new StringInfo("Q_DUR"));
        Q_AMPOutput.deliver(new StringInfo("Q_AMP"));
        P_DUROutput.deliver(new StringInfo("P_DUR"));
        P_AMPOutput.deliver(new StringInfo("P_AMP"));
        AGEOutput.deliver(new StringInfo("AGE"));
        MALEOutput.deliver(new StringInfo("MALE"));
        R_AMPOutput.deliver(new StringInfo("R_AMP"));
        S_AMPOutput.deliver(new StringInfo("S_AMP"));
        RS_RATIOOutput.deliver(new StringInfo("RS_RATIO"));
        T_AMPOutput.deliver(new StringInfo("T_AMP"));
        QRS_SUMOutput.deliver(new StringInfo("QRS_SUM"));
    }
}
