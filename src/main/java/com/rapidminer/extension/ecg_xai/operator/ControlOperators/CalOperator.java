package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.InSet;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CalOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_TYPE="Type";
    private static final String PARAMETER_LEFT="Left Operands";
    private static final String PARAMETER_PAIR="Variable Lead Pair";
    private static final String PARAMETER_MID="Operator";
    private static final String PARAMETER_RIGHT="Right Operand";
    private static final String PARAMETER_LEADS="Focused Lead";
    private static final String PARAMETER_VAR="Variable";
    private static final String PARAMETER_RESULT_NAME="Result Name";




    public CalOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
//        Boolean nodeYes=pack.yes;
        Model model=pack.getModel();

        Step step=model.getLastStep();

        String relation=getParameterAsString(PARAMETER_TYPE);
        String right=getParameterAsString(PARAMETER_RIGHT);
        String[] entryList=ParameterTypeEnumeration.transformString2Enumeration(getParameterAsString(PARAMETER_LEFT));

        String temp_entry=entryList[0];
        String[] temp_tuple=ParameterTypeTupel.transformString2Tupel(temp_entry);
        InSet temp_inset;
        if (Objects.equals(temp_tuple[1], "None")) {
            temp_inset = new InSet(temp_tuple[0]);
        }else{
            temp_inset = new InSet(temp_tuple[0], temp_tuple[1]);
        }
        Compare compare =new Compare(temp_inset.toString(),relation,right);


        for (int i = 1; i < entryList.length; i++) {
            String entry = entryList[i];
            String[] tuple = ParameterTypeTupel.transformString2Tupel(entry);
            InSet inset;
            if (Objects.equals(tuple[1], "None")) {
                inset = new InSet(tuple[0]);
            } else {
                inset = new InSet(tuple[0], tuple[1]);
            }
            compare =new Compare(compare.getLeftOperand()+inset,relation,right);
        }
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
        FeatureName featureName=new FeatureName();
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

        ParameterTypeStringCategory var=new ParameterTypeStringCategory(PARAMETER_VAR,"select the variable",featureName.getFeatures());
        ParameterTypeStringCategory leads=new ParameterTypeStringCategory(PARAMETER_LEADS,"select the leads to focus on",leadName.getLead(),"None");

        ParameterTypeTupel left_operands=new ParameterTypeTupel(PARAMETER_PAIR,"left operands",var,leads);

        ParameterTypeStringCategory mid_opearnd=new ParameterTypeStringCategory(PARAMETER_MID,"middle operand",mid);
        ParameterTypeStringCategory right_operand=new ParameterTypeStringCategory(PARAMETER_RIGHT,"right operand",right);
        ParameterTypeString result_name=new ParameterTypeString(PARAMETER_RESULT_NAME, "Define result name", "None");


        types.add(type);
        types.add(new ParameterTypeEnumeration(PARAMETER_LEFT,"left operands",left_operands));
        types.add(mid_opearnd);
        types.add(right_operand);
        types.add(result_name);
        types.add(leads);

        return types;
    }
}
