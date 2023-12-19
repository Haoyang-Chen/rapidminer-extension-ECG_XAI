package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
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

public class CalOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final InputPort first_Operand=getInputPorts().createPort("first operand");
    private final InputPort second_Operand=getInputPorts().createPort("second operand");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_TYPE ="Type";
    private static final String PARAMETER_PAIR="Variable Lead Pair";
    private static final String PARAMETER_MID="Operator";
    private static final String PARAMETER_RIGHT="Right Operand";
    private static final String PARAMETER_FIRSTLEAD="Focused Lead of First Operand";
    private static final String PARAMETER_SECONDLEAD="Focused Lead of Second Operand";
    private static final String PARAMETER_RESULT_NAME="Result Name";




    public CalOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String firstOperand=first_Operand.getData(StringInfo.class).toString();
        String secondOperand=second_Operand.getData(StringInfo.class).toString();
        String firstLead=getParameterAsString(PARAMETER_FIRSTLEAD);
        String secondLead=getParameterAsString(PARAMETER_SECONDLEAD);
        firstOperand+="_"+firstLead;
        secondOperand+="_"+secondLead;


        Pack pack=pacInput.getData(Pack.class);
//        Model model=pack.getModel();

//        Step step=model.getLastStep();
        Step step=pack.getStep();

        String relation=getParameterAsString(PARAMETER_TYPE);
        String mid=getParameterAsString(PARAMETER_MID);
        String right=getParameterAsString(PARAMETER_RIGHT);
        Compare compare=new Compare(firstOperand+relation+secondOperand,mid,right);
        this.rename(firstOperand+relation+secondOperand+mid+right);

        compare.setResultName(getParameterAsString(PARAMETER_RESULT_NAME));
        ConditionNode conditionNode=new ConditionNode(compare);


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
    public List<ParameterType> getParameterTypes() {
        LeadName leadName=new LeadName();

        String[] op_type =new String[4];
        op_type[0]="+";
        op_type[1]="-";
        op_type[2]="*";
        op_type[3]="/";

        String[] mid=new String[3];
        mid[0]=">";
        mid[1]="=";
        mid[2]="<";

        String[] right=new String[1];
        right[0]="[ENTER a NUMBER]";

        List<ParameterType> types=super.getParameterTypes();

        ParameterTypeStringCategory type=new ParameterTypeStringCategory(PARAMETER_TYPE,"select the type of this opeartor",op_type,"+");

        ParameterTypeStringCategory firstLead=new ParameterTypeStringCategory(PARAMETER_FIRSTLEAD,"select the first lead to focus on",leadName.getLead(),"None");
        ParameterTypeStringCategory secondLead=new ParameterTypeStringCategory(PARAMETER_SECONDLEAD,"select the second lead to focus on",leadName.getLead(),"None");

        ParameterTypeStringCategory mid_opearnd=new ParameterTypeStringCategory(PARAMETER_MID,"middle operand",mid);
        ParameterTypeStringCategory right_operand=new ParameterTypeStringCategory(PARAMETER_RIGHT,"right operand",right);
        ParameterTypeString result_name=new ParameterTypeString(PARAMETER_RESULT_NAME, "Define result name", "None");


        types.add(type);
        types.add(mid_opearnd);
        types.add(right_operand);
        types.add(result_name);
        types.add(firstLead);
        types.add(secondLead);

        return types;
    }
}
