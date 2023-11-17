package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;

import java.util.List;
import java.util.Map;

public class DiscretizeOperator extends Operator {
    private final InputPort inputPort = getInputPorts().createPort("In pack");
    private final InputPort featureInput = getInputPorts().createPort("Feature");
    private final OutputPort outputPort_High = getOutputPorts().createPort("High");
    private final OutputPort outputPort_Mid = getOutputPorts().createPort("Mid");
    private final OutputPort outputPort_Low = getOutputPorts().createPort("Low");
//    private static final String PARAMETER_FEATURE="Feature";
    private static final String PARAMETER_HIGH="Upper Bound";
    private static final String PARAMETER_LOW="Lower Bound";
    private static final String PARAMETER_HIGH_RESULT_NAME="High Result Name";
    private static final String PARAMETER_MID_RESULT_NAME="Mid Result Name";
    private static final String PARAMETER_LOW_RESULT_NAME="Low Result Name";
    private static final String PARAMETER_IF_HIGH="If High";
    private static final String PARAMETER_IF_MID="If Mid";
    private static final String PARAMETER_IF_LOW="If Low";

    public DiscretizeOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
//        String feature = getParameterAsString(PARAMETER_FEATURE);
        String feature = featureInput.getData(StringInfo_General.class).toString();
        String high = getParameterAsString(PARAMETER_HIGH);
        String low = getParameterAsString(PARAMETER_LOW);
        String highResultName = getParameterAsString(PARAMETER_HIGH_RESULT_NAME);
        String midResultName = getParameterAsString(PARAMETER_MID_RESULT_NAME);
        String lowResultName = getParameterAsString(PARAMETER_LOW_RESULT_NAME);
//        String ifHigh = getParameterAsString(PARAMETER_IF_HIGH);
//        String ifMid = getParameterAsString(PARAMETER_IF_MID);
//        String ifLow = getParameterAsString(PARAMETER_IF_LOW);
        Pack pack = inputPort.getData(Pack.class);
//        Boolean nodeYes = pack.yes;
        Model model = pack.getModel();

        Compare compare_low=new Compare(feature,"<",low);
        Compare compare_high=new Compare(feature,">",high);
        Compare compare_mid=new Compare(low+" <",feature,"< "+high);

        compare_low.setResultName(lowResultName);
        compare_high.setResultName(highResultName);
        compare_mid.setResultName(midResultName);

        ConditionNode conditionNode_low = new ConditionNode(compare_low);
        ConditionNode conditionNode_high = new ConditionNode(compare_high);
        ConditionNode conditionNode_mid = new ConditionNode(compare_mid);

//        Step step=model.getLastStep();
        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            conditionNode_low.addParent(parent,nodeYes);
            conditionNode_high.addParent(parent,nodeYes);
            conditionNode_mid.addParent(parent,nodeYes);
        }
//        conditionNode_low.addParent(step.getLastCon(),nodeYes);
//        conditionNode_high.addParent(step.getLastCon(),nodeYes);
//        conditionNode_mid.addParent(step.getLastCon(),nodeYes);

        Pack pack_high=new Pack(pack);
        Pack pack_mid=new Pack(pack);
        Pack pack_low=new Pack(pack);

        pack_high.getModel().getLastStep().addNode(conditionNode_low);
        pack_mid.getModel().getLastStep().addNode(conditionNode_mid);
        pack_low.getModel().getLastStep().addNode(conditionNode_high);
//
//        conditionNode_low.runCheck();
//        conditionNode_high.runCheck();
//        conditionNode_mid.runCheck();

//        if (!ifHigh.contains("--End--") && !ifHigh.contains("--MoveOn--")){
//            ImpressionNode impressionNode_high = new ImpressionNode(ifHigh);
//            impressionNode_high.addParent(conditionNode_high,true);
//            pack_high.getModel().getLastStep().addNode(impressionNode_high);
//        }
//        if (!ifMid.contains("--End--") && !ifMid.contains("--MoveOn--")){
//            ImpressionNode impressionNode_mid = new ImpressionNode(ifMid);
//            impressionNode_mid.addParent(conditionNode_mid,true);
//            pack_mid.getModel().getLastStep().addNode(impressionNode_mid);
//        }
//        if (!ifLow.contains("--End--") && !ifLow.contains("--MoveOn--")){
//            ImpressionNode impressionNode_low = new ImpressionNode(ifLow);
//            impressionNode_low.addParent(conditionNode_low,true);
//            pack_low.getModel().getLastStep().addNode(impressionNode_low);
//        }

        for (AbstractNode parent:conditionNode_high.parents){
            pack_high.current_parents.remove(parent);
        }
        pack_high.current_parents.put(conditionNode_high,true);

        for (AbstractNode parent:conditionNode_mid.parents){
            pack_mid.current_parents.remove(parent);
        }
        pack_mid.current_parents.put(conditionNode_mid,true);

        for (AbstractNode parent:conditionNode_low.parents){
            pack_low.current_parents.remove(parent);
        }
        pack_low.current_parents.put(conditionNode_low,true);

//        pack_high.setYes();
//        pack_mid.setYes();
//        pack_low.setYes();

        outputPort_High.deliver(pack_high);
        outputPort_Mid.deliver(pack_mid);
        outputPort_Low.deliver(pack_low);
    }

    @Override
    public List<ParameterType> getParameterTypes(){
//        FeatureName featureName=new FeatureName();
//        ImpressionName impressionName=new ImpressionName();
        List<ParameterType> types = super.getParameterTypes();
//        types.add(new ParameterTypeStringCategory(PARAMETER_FEATURE,"Feature to discretize",featureName.getFeatures(),null));
        types.add(new ParameterTypeString(PARAMETER_HIGH,"Upper Bound",null));
        types.add(new ParameterTypeString(PARAMETER_LOW,"Lower Bound",null));
        types.add(new ParameterTypeString(PARAMETER_HIGH_RESULT_NAME,"High Result Name",null));
        types.add(new ParameterTypeString(PARAMETER_MID_RESULT_NAME,"Mid Result Name",null));
        types.add(new ParameterTypeString(PARAMETER_LOW_RESULT_NAME,"Low Result Name",null));
//        types.add(new ParameterTypeEnumeration(PARAMETER_IF_HIGH,"If High",new ParameterTypeStringCategory(
//                PARAMETER_IF_HIGH,
//                "Choose High Path",
//                impressionName.getImpressions(),
//                "--End--"
//        )));
//        types.add(new ParameterTypeEnumeration(PARAMETER_IF_MID,"If Mid",new ParameterTypeStringCategory(
//                PARAMETER_IF_MID,
//                "Choose Mid Path",
//                impressionName.getImpressions(),
//                "--End--"
//        )));
//        types.add(new ParameterTypeEnumeration(PARAMETER_IF_LOW,"If Low",new ParameterTypeStringCategory(
//                PARAMETER_IF_LOW,
//                "Choose Low Path",
//                impressionName.getImpressions(),
//                "--End--"
//        )));
        return types;
    }

}
