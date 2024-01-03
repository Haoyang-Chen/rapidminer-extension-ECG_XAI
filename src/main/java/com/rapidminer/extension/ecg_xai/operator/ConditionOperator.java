package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.ParameterTypeStringCategory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConditionOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final InputPort leftInput=getInputPorts().createPort("left");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private final OutputPort ResultOutput=getOutputPorts().createPort("result");
    private static final String PARAMETER_MID="Operator";
    private static final String PARAMETER_RIGHT="Right Operand";
    private static final String PARAMETER_RESULT_NAME="Result Name";
    private static final String PARAMETER_LEAD="Focused Lead";


    public ConditionOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
//        String left= getParameterAsString(PARAMETER_LEFT);
        String left= leftInput.getData(StringInfo.class).toString();
        String mid= getParameterAsString(PARAMETER_MID);
        String right= getParameterAsString(PARAMETER_RIGHT);
        String resultName=getParameterAsString(PARAMETER_RESULT_NAME);
        String lead=getParameterAsString(PARAMETER_LEAD);
//        boolean yes=getParameterAsBoolean(PARAMETER_YES);
//        boolean no=getParameterAsBoolean(PARAMETER_NO);

        Pack pack=pacInput.getData(Pack.class);
//        Model model=pack.getModel();
        Compare compare;
        if (!Objects.equals(lead, "None")&&!left.contains(lead)){
            left+="_"+lead;
        }
        if (Objects.equals(lead, "None")) {
            compare = new Compare(left, mid, right);
        }else {
            compare = new Compare(left, mid, right, lead);
        }
        this.rename(compare.toString());
        if (Objects.equals(lead, "None")) {
            compare.setResultName(resultName);
        }else{
            compare.setResultName(resultName+"_"+lead);
        }
        ConditionNode conditionNode=new ConditionNode(compare);

//        Step step=model.getLastStep();
        Step step=pack.getStep();

        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            conditionNode.addParent(parent,nodeYes);
        }

        step.addNode(conditionNode);

//        conditionNode.YesMove=yes;
//        conditionNode.NoMove=no;
//        conditionNode.runNewCheck();

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
        ResultOutput.deliver(new StringInfo(resultName+'_'+step.getName()));
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        LeadName leadName=new LeadName();
        List<ParameterType> types=super.getParameterTypes();

        String[] mid=new String[3];
        mid[0]=">";
        mid[1]="=";
        mid[2]="<";

        String[] right=new String[1];
        right[0]="ENTER a NUMBER";

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
        return types;
    }
}
