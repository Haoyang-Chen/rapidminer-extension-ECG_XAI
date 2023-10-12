package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;

import java.util.List;

public class RelationOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_AND="type";
    private static final String PARAMETER_CON="conditions";
    private static final String PARAMETER_ENTRY="ENTRY";
    private static final String PARAMETER_IMP_OR_CON="is condition";
    private static final String PARAMETER_LEFT="left operand";
    private static final String PARAMETER_MID="middle operand";
    private static final String PARAMETER_RIGHT="right operand";
    private static final String PARAMETER_FEATURE="feature";
    private static final String PARAMETER_LEADS="Focused Leads";
    private static final String PARAMETER_RESULT_NAME="result name";


    public RelationOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() {

    }

    @Override
    public List<ParameterType> getParameterTypes() {
        FeatureName featureName=new FeatureName();
        ImpressionName impressionName=new ImpressionName();
        LeadName leadName=new LeadName();

        String[] op_type =new String[3];
        op_type[0]="single";
        op_type[1]="and";
        op_type[2]="or";

        String[] mid=new String[4];
        mid[0]=">";
        mid[1]="=";
        mid[2]="<";
        mid[3]="is";

        String[] right=new String[3];
        right[0]="true";
        right[1]="false";
        right[2]="[ENTER a NUMBER]";

//        ParameterTypeBoolean impOrCon=new ParameterTypeBoolean(PARAMETER_IMP_OR_CON,"condition or impression, ckecked: condition",true);
//        ParameterTypeStringCategory impression=new ParameterTypeStringCategory(PARAMETER_FEATURE,"impression to be satisfied",impressionName.getImpressions());
        ParameterTypeStringCategory left_operand=new ParameterTypeStringCategory(PARAMETER_LEFT,"Left operand",featureName.getFeatures());
        ParameterTypeStringCategory mid_opearnd=new ParameterTypeStringCategory(PARAMETER_MID,"middle operand",mid);
        ParameterTypeStringCategory right_operand=new ParameterTypeStringCategory(PARAMETER_RIGHT,"right operand",right);

//        impression.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,false));
//        left_operand.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,true));
//        mid_opearnd.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,true));
//        right_operand.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,true));

        ParameterTypeCheckBoxGroup leads=new ParameterTypeCheckBoxGroup(PARAMETER_LEADS,"select the leads to focus on");
        for (String lead: leadName.getLead()){leads.add("leads",lead);}
        leads.add("leads","Default");
        leads.setDefaultValue("Default");

        ParameterTypeTupel enrty=new ParameterTypeTupel(PARAMETER_ENTRY,"define an entry", left_operand,mid_opearnd,right_operand,leads);

        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeStringCategory(PARAMETER_AND,"select the type of this opeartor",op_type,"single"));
        types.add(new ParameterTypeEnumeration(PARAMETER_CON,"The list of conditions",enrty));
        return types;
    }
}
