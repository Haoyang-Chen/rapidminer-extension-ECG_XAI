package com.rapidminer.extension.ecg_xai.operator.AbandonedOperator;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeEnumeration;
import com.rapidminer.parameter.ParameterTypeStringCategory;

import java.util.List;
import java.util.Map;

public class ResultOperator extends Operator {
    private final InputPort Input=getInputPorts().createPort("In pack");
    private static final String PARAMETER_NAME="Result Name";
    public ResultOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws com.rapidminer.operator.OperatorException {
        String name=getParameterAsString(PARAMETER_NAME);
        Pack pack=Input.getData(Pack.class);
//        Model model=pack.getModel();
//        Step step=model.getLastStep();
        Step step=pack.getStep();
        ImpressionNode impNode=new ImpressionNode(name);
        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            impNode.addParent(parent,nodeYes);
        }
        step.addNode(impNode);
        this.rename(name);
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        ImpressionName impressionName=new ImpressionName();
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeEnumeration(PARAMETER_NAME, "The list of results", new ParameterTypeStringCategory(
                PARAMETER_NAME,
                "Choose Result Name",
                impressionName.getImpressions()
        )));
        return types;
    }
}
