package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.LogService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class RelationOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_TYPE="type";
    private static final String PARAMETER_CON="conditions";
    private static final String PARAMETER_ENTRY="ENTRY(left,mid,right operands, result name";
    private static final String PARAMETER_IMP_OR_CON="is condition";
    private static final String PARAMETER_LEFT="left operand";
    private static final String PARAMETER_MID="middle operand";
    private static final String PARAMETER_RIGHT="right operand";
    private static final String PARAMETER_FEATURE="feature";
    private static final String PARAMETER_LEADS="Focused Leads";
    private static final String PARAMETER_RESULT_NAME="result name";
    private static final String PARAMETER_YES="if yes";
    private static final String PARAMETER_NO="if no";




    public RelationOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
//        Boolean nodeYes=pack.yes;
        Model model=pack.getModel();

        Step step=model.getLastStep();

        String yes=getParameterAsString(PARAMETER_YES);
        String no=getParameterAsString(PARAMETER_NO);
        String entries=getParameterAsString(PARAMETER_CON);
        String relation=getParameterAsString(PARAMETER_TYPE);

        LogService.getRoot().log(Level.INFO,entries);
        String[] entryList=ParameterTypeEnumeration.transformString2Enumeration(getParameterAsString(PARAMETER_CON));
        String temp_entry=entryList[0];
        String[] temp_tuple=ParameterTypeTupel.transformString2Tupel(temp_entry);
        Compare temp_compare=new Compare(temp_tuple[0],temp_tuple[1],temp_tuple[2]);
        temp_compare.setResultName(temp_tuple[3]);
        ConditionNode conditionNode;
        if (!Objects.equals(relation, "single")) {
            ConditionGroup condition = new ConditionGroup();
            for (int i = 1; i < entryList.length; i++) {
                String entry = entryList[i];
                String[] tuple = ParameterTypeTupel.transformString2Tupel(entry);
                Compare compare = new Compare(tuple[0], tuple[1], tuple[2]);
                compare.setResultName(tuple[3]);
                if (condition.getLeft()==null){
                    condition=new ConditionGroup(temp_compare,compare,relation);
                }else {
                    condition = new ConditionGroup(condition, compare, relation);
                }
            }
            conditionNode = new ConditionNode(condition);
        }else{
            conditionNode = new ConditionNode(temp_compare);
        }

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
        ParameterTypeString result_name=new ParameterTypeString(PARAMETER_RESULT_NAME, "Define result name", "None");

//        impression.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,false));
//        left_operand.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,true));
//        mid_opearnd.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,true));
//        right_operand.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_IMP_OR_CON,true,true));

//        ParameterTypeCheckBoxGroup leads=new ParameterTypeCheckBoxGroup(PARAMETER_LEADS,"select the leads to focus on");
//        for (String lead: leadName.getLead()){leads.add("leads",lead);}
//        leads.add("leads","Default");

        ParameterTypeTupel enrty=new ParameterTypeTupel(PARAMETER_ENTRY,"define an entry", left_operand,mid_opearnd,right_operand,result_name);

        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeStringCategory(PARAMETER_TYPE,"select the type of this opeartor",op_type,"single"));
        types.add(new ParameterTypeEnumeration(PARAMETER_CON,"The list of conditions",enrty));

        types.add(new ParameterTypeEnumeration(PARAMETER_YES, "The list of Yes results",
                new ParameterTypeStringCategory(
                PARAMETER_YES,
                "Choose Yes Path",
                impressionName.getImpressions(),
                "--End--"
        )));
        types.add(new ParameterTypeEnumeration(PARAMETER_NO, "The list of Yes results",
                new ParameterTypeStringCategory(
                PARAMETER_NO,
                "Choose No Path",
                impressionName.getImpressions(),
                "--End--"
        )));

        return types;
    }
}
