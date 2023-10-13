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
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeEnumeration;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.ParameterTypeStringCategory;
import com.rapidminer.tools.LogService;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConditionOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_LEFT="left operand";
    private static final String PARAMETER_MID="middle operand";
    private static final String PARAMETER_RIGHT="right operand";
    private static final String PARAMETER_RESULT_NAME="result name";
    private static final String PARAMETER_YES="if yes";
    private static final String PARAMETER_NO="if no";


    public ConditionOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String left= getParameterAsString(PARAMETER_LEFT);
        String mid= getParameterAsString(PARAMETER_MID);
        String right= getParameterAsString(PARAMETER_RIGHT);
        String resultName=getParameterAsString(PARAMETER_RESULT_NAME);
        String yes=getParameterAsString(PARAMETER_YES);
        String no=getParameterAsString(PARAMETER_NO);

        Pack pack=pacInput.getData(Pack.class);
//        Boolean nodeYes=pack.yes;
        Model model=pack.getModel();
        Compare compare=new Compare(left,mid,right);
        compare.setResultName(resultName);
        ConditionNode conditionNode=new ConditionNode(compare);

        Step step=model.getLastStep();

        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            conditionNode.addParent(parent,nodeYes);
        }
//        conditionNode.addParent(step.getLastCon(),nodeYes);
        step.addNode(conditionNode);

        conditionNode.Yesres=yes;
        conditionNode.Nores=no;
        conditionNode.runCheck();

        if (!yes.contains("--End--") && !yes.contains("--MoveOn--")){
            ImpressionNode yesImp=new ImpressionNode(yes);
            yesImp.addParent(conditionNode,true);
            step.addNode(yesImp);
        }
        if (!no.contains("--End--") && !no.contains("--MoveOn--")){
            ImpressionNode noImp=new ImpressionNode(no);
            noImp.addParent(conditionNode,false);
            step.addNode(noImp);
        }

        for (AbstractNode parent:conditionNode.parents){
            pack.current_parents.remove(parent);
//            LogService.getRoot().log(Level.INFO,"AA");
        }
        pack.current_parents.put(conditionNode,true);

        Pack noPack=new Pack(pack);
        noPack.current_parents.put(conditionNode,false);
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
        types.add(new ParameterTypeEnumeration(PARAMETER_YES, "The list of Yes results", new ParameterTypeStringCategory(
                PARAMETER_YES,
                "Choose Yes Path",
                impressionName.getImpressions(),
                "--End--"
        )));
        types.add(new ParameterTypeEnumeration(PARAMETER_NO, "The list of Yes results", new ParameterTypeStringCategory(
                PARAMETER_NO,
                "Choose No Path",
                impressionName.getImpressions(),
                "--End--"
        )));
        return types;
    }
}
