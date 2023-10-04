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

    public StepStartOpeartor(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
        pack.setYes();
        Model model=pack.getModel();
        Step step=new Step();
        List<String> leads= new ArrayList<>();
        LeadName leadName = new LeadName();
        for (String lead: leadName.getLead()){
            boolean checked=getParameterAsBoolean(lead);
//            LogService.getRoot().log(Level.INFO,lead);
            if (checked){
//                LogService.getRoot().log(Level.INFO,lead);
                leads.add(lead);
            }
        }
//        System.out.println(leads);
        step.focus_leads=leads;
        model.addStep(step);
        pacOutput.deliver(pack);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        LeadName leadName = new LeadName();
        List<ParameterType> types = super.getParameterTypes();
        for (String lead: leadName.getLead()){
            types.add(new ParameterTypeBoolean(lead,null,false));
        }
        return types;
    }
}
