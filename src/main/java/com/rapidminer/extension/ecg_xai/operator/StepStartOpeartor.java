package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCheckBoxGroup;
import com.rapidminer.parameter.ParameterTypeStringCategory;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.OperatorService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static com.rapidminer.parameter.ParameterTypeCheckBoxGroup.stringToSelection;

public class StepStartOpeartor extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort pacOutput=getOutputPorts().createPort("Out pack");
    private static final String PARAMETER_LEADS="Focused Leads";

    public StepStartOpeartor(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
        pack.setYes();
        Model model=pack.getModel();
        Step step=new Step();
//        LogService.getRoot().log(Level.INFO,getName());
//        List<String> leads= new ArrayList<>();
//        LeadName leadName = new LeadName();
//        for (String lead: leadName.getLead()){
//            boolean checked=getParameterAsBoolean(lead);
////            LogService.getRoot().log(Level.INFO,lead);
//            if (checked){
////                LogService.getRoot().log(Level.INFO,lead);
//                leads.add(lead);
//            }
//        }
////        System.out.println(leads);
//        step.focus_leads=leads;
        step.focus_leads=Arrays.toString(stringToSelection(getParameterAsString(PARAMETER_LEADS)));
        model.addStep(step);
        pacOutput.deliver(pack);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        LeadName leadName = new LeadName();
        List<ParameterType> types = super.getParameterTypes();
        ParameterTypeCheckBoxGroup leads=new ParameterTypeCheckBoxGroup(PARAMETER_LEADS,"select the leads to focus on");
        for (String lead: leadName.getLead()){
//            types.add(new ParameterTypeBoolean(lead,null,false));
            leads.add("leads",lead);
        }
        types.add(leads);
        return types;
    }
}
