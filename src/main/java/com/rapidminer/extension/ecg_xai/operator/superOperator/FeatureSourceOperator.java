package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo;
import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.CollectingPortPairExtender;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.tools.Ontology;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FeatureSourceOperator extends OperatorChain {
    private final OutputPort SINUSOutput=getOutputPorts().createPort("SINUS rhythm is sinus");
    private final InputPort SINUSInput = getSubprocess(0).getInnerSinks().createPort("SINUS rhythm is sinus");
    private final OutputPort HROutput=getOutputPorts().createPort("HR heart rate");
    private final InputPort HRInput = getSubprocess(0).getInnerSinks().createPort("HR heart rate");
    private final OutputPort RR_DIFFOutput=getOutputPorts().createPort("RR_DIFF RR interval");
    private final InputPort RR_DIFFInput = getSubprocess(0).getInnerSinks().createPort("RR_DIFF RR interval");
    private final OutputPort QRS_DUROutput=getOutputPorts().createPort("QRS_DUR QRS duration");
    private final InputPort QRS_DURInput = getSubprocess(0).getInnerSinks().createPort("QRS_DUR QRS duration");
    private final OutputPort PR_DUROutput=getOutputPorts().createPort("PR_DUR PR duration");
    private final InputPort PR_DURInput = getSubprocess(0).getInnerSinks().createPort("PR_DUR PR duration");
    private final OutputPort ST_AMPOutput=getOutputPorts().createPort("ST_AMP ST amplitude");
    private final InputPort ST_AMPInput = getSubprocess(0).getInnerSinks().createPort("ST_AMP ST amplitude");
    private final OutputPort Q_DUROutput=getOutputPorts().createPort("Q_DUR Q wave duration");
    private final InputPort Q_DURInput = getSubprocess(0).getInnerSinks().createPort("Q_DUR Q wave duration");
    private final OutputPort Q_AMPOutput=getOutputPorts().createPort("Q_AMP Q wave amplitude");
    private final InputPort Q_AMPInput = getSubprocess(0).getInnerSinks().createPort("Q_AMP Q wave amplitude");
    private final OutputPort PRWPOutput=getOutputPorts().createPort("PRWP Poor R wave progression");
    private final InputPort PRWPInput = getSubprocess(0).getInnerSinks().createPort("PRWP Poor R wave progression");
    private final OutputPort P_DUROutput=getOutputPorts().createPort("P_DUR P wave duration");
    private final InputPort P_DURInput = getSubprocess(0).getInnerSinks().createPort("P_DUR P wave duration");
    private final OutputPort P_AMPOutput=getOutputPorts().createPort("P_AMP P wave amplitude");
    private final InputPort P_AMPInput = getSubprocess(0).getInnerSinks().createPort("P_AMP P wave amplitude");
    private final OutputPort AGEOutput=getOutputPorts().createPort("AGE");
    private final InputPort AGEInput = getSubprocess(0).getInnerSinks().createPort("AGE");
    private final OutputPort MALEOutput=getOutputPorts().createPort("MALE");
    private final InputPort MALEInput = getSubprocess(0).getInnerSinks().createPort("MALE");
    private final OutputPort R_AMPOutput=getOutputPorts().createPort("R_AMP R wave amplitude");
    private final InputPort R_AMPInput = getSubprocess(0).getInnerSinks().createPort("R_AMP R wave amplitude");
    private final OutputPort S_AMPOutput=getOutputPorts().createPort("S_AMP S wave amplitude");
    private final InputPort S_AMPInput = getSubprocess(0).getInnerSinks().createPort("S_AMP S wave amplitude");
    private final OutputPort RS_RATIOOutput=getOutputPorts().createPort("RS_RATIO R/S wave amplitude ratio");
    private final InputPort RS_RATIOInput = getSubprocess(0).getInnerSinks().createPort("RS_RATIO R/S wave amplitude ratio");
    private final OutputPort RADOutput=getOutputPorts().createPort("RAD right axis deviation");
    private final InputPort RADInput = getSubprocess(0).getInnerSinks().createPort("RAD right axis deviation");
    private final OutputPort T_AMPOutput=getOutputPorts().createPort("T_AMP T wave amplitude");
    private final InputPort T_AMPInput = getSubprocess(0).getInnerSinks().createPort("T_AMP T wave amplitude");
    private final OutputPort QRS_SUMOutput=getOutputPorts().createPort("QRS_SUM");
    private final InputPort QRS_SUMInput = getSubprocess(0).getInnerSinks().createPort("QRS_SUM");

    private final CollectingPortPairExtender outExtender =
            new CollectingPortPairExtender("other",
                    getSubprocess(0).getInnerSinks(), getOutputPorts());

    private final OutputPort SUMMARY_port=getOutputPorts().createPort("Summary gives a summary of all the features");

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

    public void putSum(Set<String> features,StringInfo info){
        if (info.type.equals("General")){
            features.add("'"+info+"'");
        }else {
            features.add("'"+info+"(lead)"+"'");
        }
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        getSubprocess(0).execute();
        Set<String> features=new HashSet<>();
        if(SINUSInput.isConnected()) {
            SINUSOutput.deliver(SINUSInput.getData(StringInfo.class));
            putSum(features,SINUSInput.getData(StringInfo.class));
        }
        if(HRInput.isConnected()) {
            HROutput.deliver(HRInput.getData(StringInfo.class));
            putSum(features,HRInput.getData(StringInfo.class));
        }
        if(RR_DIFFInput.isConnected()) {
            RR_DIFFOutput.deliver(RR_DIFFInput.getData(StringInfo.class));
            putSum(features,RR_DIFFInput.getData(StringInfo.class));
        }
        if(QRS_DURInput.isConnected()) {
            QRS_DUROutput.deliver(QRS_DURInput.getData(StringInfo.class));
            putSum(features,QRS_DURInput.getData(StringInfo.class));
        }
        if(PR_DURInput.isConnected()) {
            PR_DUROutput.deliver(PR_DURInput.getData(StringInfo.class));
            putSum(features,PR_DURInput.getData(StringInfo.class));
        }
        if(ST_AMPInput.isConnected()) {
            ST_AMPOutput.deliver(ST_AMPInput.getData(StringInfo.class));
            putSum(features,ST_AMPInput.getData(StringInfo.class));
        }
        if(Q_DURInput.isConnected()) {
            Q_DUROutput.deliver(Q_DURInput.getData(StringInfo.class));
            putSum(features,Q_DURInput.getData(StringInfo.class));
        }
        if(Q_AMPInput.isConnected()) {
            Q_AMPOutput.deliver(Q_AMPInput.getData(StringInfo.class));
            putSum(features,Q_AMPInput.getData(StringInfo.class));
        }
        if(PRWPInput.isConnected()) {
            PRWPOutput.deliver(PRWPInput.getData(StringInfo.class));
            putSum(features,PRWPInput.getData(StringInfo.class));
        }
        if(P_DURInput.isConnected()) {
            P_DUROutput.deliver(P_DURInput.getData(StringInfo.class));
            putSum(features,P_DURInput.getData(StringInfo.class));
        }
        if(P_AMPInput.isConnected()) {
            P_AMPOutput.deliver(P_AMPInput.getData(StringInfo.class));
            putSum(features,P_AMPInput.getData(StringInfo.class));
        }
        if(AGEInput.isConnected()) {
            AGEOutput.deliver(AGEInput.getData(StringInfo.class));
            putSum(features,AGEInput.getData(StringInfo.class));
        }
        if(MALEInput.isConnected()) {
            MALEOutput.deliver(MALEInput.getData(StringInfo.class));
            putSum(features,MALEInput.getData(StringInfo.class));
        }
        if(R_AMPInput.isConnected()) {
            R_AMPOutput.deliver(R_AMPInput.getData(StringInfo.class));
            putSum(features,R_AMPInput.getData(StringInfo.class));
        }
        if(S_AMPInput.isConnected()) {
            S_AMPOutput.deliver(S_AMPInput.getData(StringInfo.class));
            putSum(features,S_AMPInput.getData(StringInfo.class));
        }
        if(RS_RATIOInput.isConnected()) {
            RS_RATIOOutput.deliver(RS_RATIOInput.getData(StringInfo.class));
            putSum(features,RS_RATIOInput.getData(StringInfo.class));
        }
        if(RADInput.isConnected()) {
            RADOutput.deliver(RADInput.getData(StringInfo.class));
            putSum(features,RADInput.getData(StringInfo.class));
        }
        if(T_AMPInput.isConnected()) {
            T_AMPOutput.deliver(T_AMPInput.getData(StringInfo.class));
            putSum(features,T_AMPInput.getData(StringInfo.class));
        }
        if(QRS_SUMInput.isConnected()) {
            QRS_SUMOutput.deliver(QRS_SUMInput.getData(StringInfo.class));
            putSum(features,QRS_SUMInput.getData(StringInfo.class));
        }
        outExtender.collect();
        List<StringInfo> others = outExtender.getData(StringInfo.class, true);
        for (StringInfo other : others) {
//            LogService.getRoot().log(Level.INFO,other.type);
            if (other.type.equals("General")){
                features.add("'"+other+"'");
            }else {
                features.add("'"+other+"(lead)"+"'");
            }
        }
        com.rapidminer.example.Attribute[] attributes = new Attribute[1];

        attributes[0] = AttributeFactory.createAttribute ("features", Ontology.STRING);

        MemoryExampleTable table = new MemoryExampleTable(attributes);
        DataRowFactory ROW_FACTORY = new DataRowFactory(0);
        String[] data = new String[1];

        data[0] = features.toString();
        DataRow row = ROW_FACTORY.create(data, attributes);
        table.addDataRow(row);
        ExampleSet exampleSet = table.createExampleSet();
        SUMMARY_port.deliver(exampleSet);
    }

}
