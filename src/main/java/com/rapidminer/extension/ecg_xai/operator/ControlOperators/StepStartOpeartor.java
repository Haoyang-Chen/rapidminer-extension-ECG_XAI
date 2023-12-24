package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.OutputPortExtender;
import com.rapidminer.parameter.*;

import java.util.Arrays;
import java.util.List;

import static com.rapidminer.parameter.ParameterTypeCheckBoxGroup.stringToSelection;

public class StepStartOpeartor extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPortExtender outputPortExtender = new OutputPortExtender("Out pack", getOutputPorts());
//    private static final String PARAMETER_LEADS="Focused Leads";
    public static final String PARAMETER_NAME="Step Name";

    public StepStartOpeartor(OperatorDescription description) {
        super(description);
        outputPortExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
        String name=getParameterAsString(PARAMETER_NAME);
        Pack pack=pacInput.getData(Pack.class);
        pack.current_parents.clear();
//        Model model=pack.getModel();
//        Pack pack=new Pack();
        Step step=new Step();
        step.setName(name);
        this.rename(name);

//        step.focus_leads=Arrays.toString(stringToSelection(getParameterAsString(PARAMETER_LEADS)));
//        model.addStep(step);
        pack.current_parents.put(step.nodes.get(0),true);
        pack.step=step;

        for (OutputPort outputPort : outputPortExtender.getManagedPorts()) {
            outputPort.deliver(new Pack(pack));
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        LeadName leadName = new LeadName();
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeString(PARAMETER_NAME,"Name of the step","Step"));
//        ParameterTypeCheckBoxGroup leads=new ParameterTypeCheckBoxGroup(PARAMETER_LEADS,"select the leads to focus on");
//        for (String lead: leadName.getLead()){
//            leads.add("leads",lead);
//        }
//        types.add(leads);
        return types;
    }
}
