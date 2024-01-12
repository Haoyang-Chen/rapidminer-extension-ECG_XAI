package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.NoPack;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.YesPack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.AtLeastNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.GenerateNewMDRule;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.EqualStringCondition;

import java.util.List;
import java.util.Map;

public class CombineOperator extends Operator {
    private final InputPortExtender inputPortExtender = new InputPortExtender("In pack", getInputPorts());
    private final OutputPort yesOutput=getOutputPorts().createPort("yes");
    private final OutputPort noOutput=getOutputPorts().createPort("no");
    private static final String PARAMETER_TYPE="Type";
    private static final String PARAMETER_NUM="At Least () Satisfied";
    private static final String PARAMETER_RESULT_NAME="Result Name";
    private static final String PARAMETER_RE="Relation";
    public CombineOperator(OperatorDescription description) {
        super(description);
        inputPortExtender.start();
        getTransformer().addRule(new GenerateNewMDRule(yesOutput, YesPack.class));
        getTransformer().addRule(new GenerateNewMDRule(noOutput, NoPack.class));
    }

    @Override
    public void doWork() throws OperatorException {
        List<Pack> packs = inputPortExtender.getData(Pack.class, true);
        Pack pack= new Pack(packs.get(0));
//        Model model=pack.getModel();
//        Step step=model.getLastStep();
        Step step=pack.getStep();

        String type=getParameterAsString(PARAMETER_TYPE);

        AbstractNode conditionNode=null;
        switch (type){
            case "Relation":
                String result_name=getParameterAsString(PARAMETER_RESULT_NAME);
                String relation=getParameterAsString(PARAMETER_RE);
                ConditionGroup conditionGroup=new ConditionGroup();
                conditionGroup.setResultName(result_name);
                conditionNode=new ConditionNode(conditionGroup);

                boolean parentCondition=false;

                for (Pack temp_pack:packs){
                    if (temp_pack!=pack){
                        Map<AbstractNode, Boolean> current_parents = temp_pack.current_parents;
                        for (Map.Entry<AbstractNode, Boolean> entry : current_parents.entrySet()) {
                            AbstractNode node = entry.getKey();
                            for (AbstractNode parent : node.parents) {
                                for (AbstractNode temp_node:pack.current_parents.keySet()) {
                                    if ((parent.YesSon.contains(node) && parent.NoSon.contains(temp_node)) || (parent.NoSon.contains(node) && parent.YesSon.contains(temp_node))) {
                                        parentCondition = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                for (Pack temp_pack:packs) {
                    Map<AbstractNode, Boolean> current_parents = temp_pack.current_parents;
                    for (Map.Entry<AbstractNode, Boolean> entry : current_parents.entrySet()) {
                        AbstractNode node = entry.getKey();
                        step.nodes.remove(node);
                        AbstractCondition condition = node.getCondition();

                        ConditionGroup tempCG=new ConditionGroup();
                        for (AbstractNode parent:node.parents){

                            if (parentCondition){
                                if (parent.YesSon.contains(node)) {
                                    tempCG.setRight(parent.getCondition());
                                    tempCG.setRelation("AND");
                                    parent.YesSon.remove(node);
                                }else if (parent.NoSon.contains(node)){
                                    AbstractCondition parentCon=parent.getCondition();
                                    if (parentCon instanceof Compare) {
                                        Compare tempCon;
                                        if (parentCon.getLead()==null) {
                                            tempCon = new Compare(parentCon.getLeftOperand(), parentCon.getMidOperand(), parentCon.getRightOperand());
                                        }else{
                                            tempCon = new Compare(parentCon.getLeftOperand(), parentCon.getMidOperand(), parentCon.getRightOperand(),parentCon.getLead());
                                        }
                                        tempCon.setResultName("~"+parentCon.getResultName());
                                    }
                                    tempCG.setRight(parent.getCondition());
                                    tempCG.setRelation("AND ~");
                                    parent.NoSon.remove(node);
                                }
                                tempCG.setLeft(condition);
//                                parentCondition=true;
                            }
                        }
                        if (parentCondition){
                            if (conditionGroup.getLeft() == null) {
                                conditionGroup.setLeft(tempCG);
                            } else if (conditionGroup.getRight() == null) {
                                conditionGroup.setRelation(relation);
                                conditionGroup.setRight(tempCG);
                            } else {
                                conditionGroup = new ConditionGroup(conditionGroup, tempCG, relation);
                                ((ConditionNode) conditionNode).setCondition(conditionGroup);
                            }
                        }else {
                            if (conditionGroup.getLeft() == null) {
                                conditionGroup.setLeft(condition);
                            } else if (conditionGroup.getRight() == null) {
                                conditionGroup.setRelation(relation);
                                conditionGroup.setRight(condition);
                            } else {
                                conditionGroup = new ConditionGroup(conditionGroup, condition, relation);
                                ((ConditionNode) conditionNode).setCondition(conditionGroup);
                            }
                        }
                        for (AbstractNode parent :node.parents){
                            if (parentCondition){
                                for (AbstractNode grandparent:parent.parents){
                                    grandparent.YesSon.remove(parent);
                                    conditionNode.addParent(grandparent,true);
                                }
                            }
                            if(parent.YesSon.contains(node)){
                                parent.YesSon.remove(node);
                                conditionNode.addParent(parent,true);
                            }
                            if(parent.NoSon!=null&& parent.NoSon.contains(node)){
                                parent.NoSon.add(conditionNode);
                                parent.NoSon.remove(node);
                                conditionNode.addParent(parent,false);
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

        step.addNode(conditionNode);
        pack.current_parents.clear();
        Pack noPack=new Pack(pack);
        pack.current_parents.put(conditionNode,true);
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

//        types.add(new ParameterTypeBoolean(PARAMETER_YES, "If Yes Move On", false, false));
//        types.add(new ParameterTypeBoolean(PARAMETER_NO, "If No Move On", false, false));
        return types;
    }
}
