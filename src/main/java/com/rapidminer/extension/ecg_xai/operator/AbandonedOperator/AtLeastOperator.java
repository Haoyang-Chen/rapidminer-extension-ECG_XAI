package com.rapidminer.extension.ecg_xai.operator.AbandonedOperator;

import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.AtLeastNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AtLeastOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_NUM="At Least () Satisfied";
    private static final String PARAMETER_CON="Conditions";
    private static final String PARAMETER_ENTRY="ENTRY(left,mid,right operands, result name, lead)";
    private static final String PARAMETER_LEFT="Left Operand";
    private static final String PARAMETER_MID="Middle Operand";
    private static final String PARAMETER_RIGHT="Right Operand";
    private static final String PARAMETER_RESULT_NAME="Result Name";
    private static final String PARAMETER_LEADS="Focused Leads";
    private static final String PARAMETER_YES="If Yes";
    private static final String PARAMETER_NO="If No";

    public AtLeastOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
        Model model=pack.getModel();

        Step step=model.getLastStep();

        String yes=getParameterAsString(PARAMETER_YES);
        String no=getParameterAsString(PARAMETER_NO);
        int num=getParameterAsInt(PARAMETER_NUM);

        String[] entryList=ParameterTypeEnumeration.transformString2Enumeration(getParameterAsString(PARAMETER_CON));
        AtLeastNode atLeastNode =new AtLeastNode();
        atLeastNode.setNum(num);

        for (String entry : entryList) {
            String[] tuple = ParameterTypeTupel.transformString2Tupel(entry);
            Compare compare;
            if (Objects.equals(tuple[4], "None")) {
                compare = new Compare(tuple[0], tuple[1], tuple[2]);
            }else{
                compare = new Compare(tuple[0], tuple[1], tuple[2],tuple[4]);
            }
            compare.setResultName(tuple[3]);
            atLeastNode.addCondition(compare);
        }

        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            atLeastNode.addParent(parent,nodeYes);
        }

        step.addNode(atLeastNode);
        atLeastNode.Yesres=yes;
        atLeastNode.Nores=no;
        atLeastNode.runCheck();

        if (!yes.contains("--End--") && !yes.contains("--MoveOn--")){
            ImpressionNode yesImp=new ImpressionNode(yes);
            yesImp.addParent(atLeastNode,true);
            step.addNode(yesImp);
        }
        if (!no.contains("--End--") && !no.contains("--MoveOn--")){
            ImpressionNode noImp=new ImpressionNode(no);
            noImp.addParent(atLeastNode,false);
            step.addNode(noImp);
        }

        for (AbstractNode parent: atLeastNode.parents){
            pack.current_parents.remove(parent);
        }
        pack.current_parents.put(atLeastNode,true);
        Pack noPack=new Pack(pack);
        noPack.current_parents.put(atLeastNode,false);
        yesOutput.deliver(pack);
        noOutput.deliver(noPack);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        FeatureName featureName=new FeatureName();
        LeadName leadName=new LeadName();
        ImpressionName impressionName=new ImpressionName();

        String[] mid=new String[4];
        mid[0]=">";
        mid[1]="=";
        mid[2]="<";
        mid[3]="is";

        String[] right=new String[3];
        right[0]="true";
        right[1]="false";
        right[2]="[ENTER a NUMBER]";

        ParameterTypeStringCategory left_operand=new ParameterTypeStringCategory(PARAMETER_LEFT,"Left operand",featureName.getFeatures());
        ParameterTypeStringCategory mid_opearnd=new ParameterTypeStringCategory(PARAMETER_MID,"middle operand",mid);
        ParameterTypeStringCategory right_operand=new ParameterTypeStringCategory(PARAMETER_RIGHT,"right operand",right);
        ParameterTypeString result_name=new ParameterTypeString(PARAMETER_RESULT_NAME, "Define result name", "None");

        ParameterTypeStringCategory leads=new ParameterTypeStringCategory(PARAMETER_LEADS,"select the leads to focus on",leadName.getLead(),"None");

        ParameterTypeTupel enrty=new ParameterTypeTupel(PARAMETER_ENTRY,"define an entry", left_operand,mid_opearnd,right_operand,result_name,leads);

        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeInt(PARAMETER_NUM,"At least () is true",1,10,2));
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

