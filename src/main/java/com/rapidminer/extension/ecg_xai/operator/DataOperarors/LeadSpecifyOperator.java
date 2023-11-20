package com.rapidminer.extension.ecg_xai.operator.DataOperarors;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo_Lead;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

public class LeadSpecifyOperator extends Operator {
    private final InputPort Input=getInputPorts().createPort("Feature");
    private final OutputPort I_output=getOutputPorts().createPort("I  lead I");
    private final OutputPort II_output=getOutputPorts().createPort("II  lead II");
    private final OutputPort III_output=getOutputPorts().createPort("III  lead III");
    private final OutputPort aVR_output=getOutputPorts().createPort("aVR  lead aVR");
    private final OutputPort aVL_output=getOutputPorts().createPort("aVL  lead aVL");
    private final OutputPort aVF_output=getOutputPorts().createPort("aVF  lead aVF");
    private final OutputPort V1_output=getOutputPorts().createPort("V1  lead V1");
    private final OutputPort V2_output=getOutputPorts().createPort("V2  lead V2");
    private final OutputPort V3_output=getOutputPorts().createPort("V3  lead V3");
    private final OutputPort V4_output=getOutputPorts().createPort("V4  lead V4");
    private final OutputPort V5_output=getOutputPorts().createPort("V5  lead V5");
    private final OutputPort V6_output=getOutputPorts().createPort("V6  lead V6");

    public LeadSpecifyOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws com.rapidminer.operator.OperatorException {
        String feature=Input.getData(StringInfo_Lead.class).toString();
        I_output.deliver(new StringInfo(feature+"_I"));
        II_output.deliver(new StringInfo(feature+"_II"));
        III_output.deliver(new StringInfo(feature+"_III"));
        aVR_output.deliver(new StringInfo(feature+"_aVR"));
        aVL_output.deliver(new StringInfo(feature+"_aVL"));
        aVF_output.deliver(new StringInfo(feature+"_aVF"));
        V1_output.deliver(new StringInfo(feature+"_V1"));
        V2_output.deliver(new StringInfo(feature+"_V2"));
        V3_output.deliver(new StringInfo(feature+"_V3"));
        V4_output.deliver(new StringInfo(feature+"_V4"));
        V5_output.deliver(new StringInfo(feature+"_V5"));
        V6_output.deliver(new StringInfo(feature+"_V6"));
    }
}
