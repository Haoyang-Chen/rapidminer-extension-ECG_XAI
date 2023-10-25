package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Exist;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.tools.LogService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class ExistOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_LEFT="Variable";
    private static final String PARAMETER_LEAD="Focused Lead";


    public ExistOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String left= getParameterAsString(PARAMETER_LEFT);
        String lead=getParameterAsString(PARAMETER_LEAD);

        Pack pack=pacInput.getData(Pack.class);
        Model model=pack.getModel();
        Exist exist;
        if (Objects.equals(lead, "None")) {
            exist = new Exist(left);
        }else {
            exist = new Exist(left, lead);
        }
        this.rename(exist.toString());
        ConditionNode conditionNode=new ConditionNode(exist);

        Step step=model.getLastStep();

        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            conditionNode.addParent(parent,nodeYes);
        }

        step.addNode(conditionNode);

        for (AbstractNode parent:conditionNode.parents){
            pack.current_parents.remove(parent);
        }
        Pack noPack=new Pack(pack);
        pack.current_parents.put(conditionNode,true);
        noPack.current_parents.put(conditionNode,false);
        yesOutput.deliver(pack);
        noOutput.deliver(noPack);
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        ImpressionName impressionName=new ImpressionName();
        LeadName leadName=new LeadName();
        List<ParameterType> types=super.getParameterTypes();


        types.add(new ParameterTypeStringCategory(
                PARAMETER_LEFT,
                "Choose variable",
                impressionName.getImpressions()
        ));
        types.add(new ParameterTypeStringCategory(
                PARAMETER_LEAD,
                "select the lead to focus on, can be None",
                leadName.getLead(),
                "None"
        ));
        return types;
    }
}
