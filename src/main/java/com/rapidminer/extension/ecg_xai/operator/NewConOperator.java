package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
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

public class NewConOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_LEFT="Left Operand";
    private static final String PARAMETER_MID="Middle Operand";
    private static final String PARAMETER_RIGHT="Right Operand";
    private static final String PARAMETER_RESULT_NAME="Result Name";
    private static final String PARAMETER_LEAD="Focused Lead";
    private static final String PARAMETER_YES="If Yes Move On";
    private static final String PARAMETER_NO="If No Move On";


    public NewConOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String left= getParameterAsString(PARAMETER_LEFT);
        String mid= getParameterAsString(PARAMETER_MID);
        String right= getParameterAsString(PARAMETER_RIGHT);
        String resultName=getParameterAsString(PARAMETER_RESULT_NAME);
        String lead=getParameterAsString(PARAMETER_LEAD);
        boolean yes=getParameterAsBoolean(PARAMETER_YES);
        boolean no=getParameterAsBoolean(PARAMETER_NO);

        Pack pack=pacInput.getData(Pack.class);
        Model model=pack.getModel();
        Compare compare;
        if (Objects.equals(lead, "None")) {
            compare = new Compare(left, mid, right);
        }else {
            compare = new Compare(left, mid, right, lead);
        }
        compare.setResultName(resultName);
        ConditionNode conditionNode=new ConditionNode(compare);

        Step step=model.getLastStep();

        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            conditionNode.addParent(parent,nodeYes);
        }

        step.addNode(conditionNode);

        conditionNode.YesMove=yes;
        conditionNode.NoMove=no;
        conditionNode.runNewCheck();

//        if (!yes.contains("--End--") && !yes.contains("--MoveOn--")){
//            ImpressionNode yesImp=new ImpressionNode(yes);
//            yesImp.addParent(conditionNode,true);
//            step.addNode(yesImp);
//        }
//        if (!no.contains("--End--") && !no.contains("--MoveOn--")){
//            ImpressionNode noImp=new ImpressionNode(no);
//            noImp.addParent(conditionNode,false);
//            step.addNode(noImp);
//        }

        for (AbstractNode parent:conditionNode.parents){
            pack.current_parents.remove(parent);
        }
        Pack noPack=new Pack(pack);
        pack.current_parents.put(conditionNode,true);
        noPack.current_parents.put(conditionNode,false);
//        LogService.getRoot().log(Level.INFO,pack.current_parents.toString());
//        LogService.getRoot().log(Level.INFO,noPack.current_parents.toString());
        yesOutput.deliver(pack);
        noOutput.deliver(noPack);
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        FeatureName featureName=new FeatureName();
        ImpressionName impressionName=new ImpressionName();
        LeadName leadName=new LeadName();
        List<ParameterType> types=super.getParameterTypes();

        String[] mid=new String[4];
        mid[0]=">";
        mid[1]="=";
        mid[2]="<";
        mid[3]="is";

        String[] right=new String[3];
        right[0]="true";
        right[1]="false";
        right[2]="ENTER a NUMBER";

        types.add(new ParameterTypeStringCategory(
                PARAMETER_LEFT,
                "Choose left operand",
                featureName.getFeatures()
        ));
        types.add(new ParameterTypeStringCategory(
                PARAMETER_MID,
                "Choose mid operand",
                mid
        ));
        types.add(new ParameterTypeStringCategory(
                PARAMETER_RIGHT,
                "Choose right operand",
                right
        ));
        types.add(new ParameterTypeString(
                PARAMETER_RESULT_NAME,
                "Define result name",
                "None"
        ));
        types.add(new ParameterTypeStringCategory(
                PARAMETER_LEAD,
                "select the lead to focus on, can be None",
                leadName.getLead(),
                "None"
        ));
        types.add(new ParameterTypeBoolean(PARAMETER_YES, "If Yes Move On", false, false));
        types.add(new ParameterTypeBoolean(PARAMETER_NO, "If No Move On", false, false));

        return types;
    }
}
