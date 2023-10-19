package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.AtLeastNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;
import com.rapidminer.io.process.conditions.ParameterEqualsCondition;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.parameter.conditions.EqualStringCondition;
import com.rapidminer.tools.OperatorService;

import java.util.List;
import java.util.Map;

public class CombineOperator extends Operator {
    private final InputPortExtender inputPortExtender = new InputPortExtender("In pack", getInputPorts());
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_TYPE="Type";

    private static final String PARAMETER_YES="If Yes";
    private static final String PARAMETER_NO="If No";
    private static final String PARAMETER_NUM="At Least () Satisfied";
    private static final String PARAMETER_RESULT_NAME="Result Name";
    private static final String PARAMETER_RE="Relation";
    public CombineOperator(OperatorDescription description) {
        super(description);
        inputPortExtender.ensureMinimumNumberOfPorts(1);
        inputPortExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
        List<Pack> packs = inputPortExtender.getData(Pack.class, true);
        Pack pack= new Pack(packs.get(0));
        Model model=pack.getModel();
        Step step=model.getLastStep();

        String yes=getParameterAsString(PARAMETER_YES);
        String no=getParameterAsString(PARAMETER_NO);

        String type=getParameterAsString(PARAMETER_TYPE);

        AbstractNode conditionNode=null;
        switch (type){
            case "Relation":
                String result_name=getParameterAsString(PARAMETER_RESULT_NAME);
                String relation=getParameterAsString(PARAMETER_RE);
                ConditionGroup conditionGroup=new ConditionGroup();
                conditionGroup.setResultName(result_name);
                conditionNode=new ConditionNode(conditionGroup);;
                for (Pack temp_pack:packs) {
                    Map<AbstractNode, Boolean> current_parents = temp_pack.current_parents;
                    for (Map.Entry<AbstractNode, Boolean> entry : current_parents.entrySet()) {
                        AbstractNode node = entry.getKey();
                        step.nodes.remove(node);
                        AbstractCondition condition = node.getCondition();
                        if (conditionGroup.getLeft()==null){
                            conditionGroup.setLeft(condition);
                        } else if (conditionGroup.getRight()==null) {
                            conditionGroup.setRelation(relation);
                            conditionGroup.setRight(condition);
                        } else{
                            conditionGroup=new ConditionGroup(conditionGroup,condition,relation);
                        }
                        for (AbstractNode grandParent:node.parents){
                            if(grandParent.YesSon.contains(node)){
                                grandParent.YesSon.remove(node);
                                conditionNode.addParent(grandParent,true);
                            }
                            if(grandParent.NoSon!=null&&grandParent.NoSon.contains(node)){
                                grandParent.NoSon.add(conditionNode);
                                grandParent.NoSon.remove(node);
                                conditionNode.addParent(grandParent,false);
                            }
                        }
                    }
                }

                break;
            case "At Least":
                int num=getParameterAsInt(PARAMETER_NUM);
                conditionNode =new AtLeastNode();
                ((AtLeastNode) conditionNode).setNum(num);
                for (Pack temp_pack:packs){
                    Map<AbstractNode,Boolean> current_parents=temp_pack.current_parents;
                    for (Map.Entry<AbstractNode, Boolean> entry : current_parents.entrySet()){
                        AbstractNode node=entry.getKey();
                        step.nodes.remove(node);
                        AbstractCondition condition=node.getCondition();
                        ((AtLeastNode) conditionNode).addCondition(condition);
                        for (AbstractNode grandParent:node.parents){
                            if(grandParent.YesSon.contains(node)){
                                grandParent.YesSon.remove(node);
                                conditionNode.addParent(grandParent,true);
                            }
                            if(grandParent.NoSon.contains(node)){
                                grandParent.NoSon.add(conditionNode);
                                grandParent.NoSon.remove(node);
                                conditionNode.addParent(grandParent,false);
                            }
                        }
                    }
                }

                break;
        }

        conditionNode.Yesres=yes;
        conditionNode.Nores=no;
        step.addNode(conditionNode);
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
        pack.current_parents.put(conditionNode,true);
        Pack noPack=new Pack(pack);
        noPack.current_parents.put(conditionNode,false);
        yesOutput.deliver(pack);
        noOutput.deliver(noPack);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        String[] type=new String[2];
        type[0]="Relation";
        type[1]="At Least";

        String[] relation_type =new String[2];
        relation_type[0]="and";
        relation_type[1]="or";

        ImpressionName impressionName=new ImpressionName();
        List<ParameterType> types = super.getParameterTypes();
        ParameterTypeStringCategory opType=new ParameterTypeStringCategory(PARAMETER_TYPE, "Choose Combination Type", type);
        ParameterTypeStringCategory reType=new ParameterTypeStringCategory(PARAMETER_RE, "Choose Relation Type", relation_type);
        ParameterTypeInt numType=new ParameterTypeInt(PARAMETER_NUM,"At least () is true",1,10,2);
        ParameterTypeString result_name=new ParameterTypeString(PARAMETER_RESULT_NAME, "Define result name", "None");

        reType.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "Relation"));
        numType.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "At Least"));
        result_name.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "Relation"));

        types.add(opType);
        types.add(reType);
        types.add(numType);
        types.add(result_name);

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
