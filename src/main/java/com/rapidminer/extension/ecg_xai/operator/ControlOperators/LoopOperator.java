package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.*;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.AtLeastNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.*;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.EqualStringCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoopOperator extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    public final InputPort OutModelInput = getSubprocess(0).getInnerSinks().createPort("Model");
    public final OutputPort OutModelOutput=getOutputPorts().createPort("Model");

    public final PortPairExtender inExtender =
            new PortPairExtender("Input",
                    getInputPorts(), getSubprocess(0).getInnerSources());

    private static final String PARAMETER_LOOP="Loop Over";

    private static final String PARAMETER_TYPE="Type";
    private static final String PARAMETER_NUM="At Least () Satisfied";
    private static final String PARAMETER_RE="Relation";

    public LoopOperator(OperatorDescription description) {
        super(description, "Executed Process");
        inExtender.start();
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(InModelInput, OutModelOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        String[] elements=getParameterAsString(PARAMETER_LOOP).split(",");
        String type=getParameterAsString(PARAMETER_TYPE);
        List<Pack> packs=new ArrayList<>();
        for(String element:elements){
            List<StringInfo> inputs=inExtender.getData(StringInfo.class, true);
            for (StringInfo input:inputs){
                input.info+="_"+element;
            }
            inExtender.deliver(inputs);
            getSubprocess(0).execute();
            for (StringInfo input:inputs){
                input.info=input.info.substring(0,input.info.length()-element.length()-1);
            }
            packs.add(OutModelInput.getData(Pack.class));
        }
        Pack pack= new Pack(packs.get(0));
//        Model model=pack.getModel();
//        Step step=model.getLastStep();
        Step step=pack.getStep();

        AbstractNode conditionNode=null;
        switch (type) {
            case "Relation":
                String relation = getParameterAsString(PARAMETER_RE);
                ConditionGroup conditionGroup = new ConditionGroup();
                conditionNode = new ConditionNode(conditionGroup);
                for (Pack temp_pack : packs) {
                    Map<AbstractNode, Boolean> current_parents = temp_pack.current_parents;
                    for (Map.Entry<AbstractNode, Boolean> entry : current_parents.entrySet()) {
                        AbstractNode node = entry.getKey();
                        step.nodes.remove(node);
                        AbstractCondition condition = node.getCondition();
                        if (conditionGroup.getLeft() == null) {
                            conditionGroup.setLeft(condition);
                        } else if (conditionGroup.getRight() == null) {
                            conditionGroup.setRelation(relation);
                            conditionGroup.setRight(condition);
                        } else {
                            conditionGroup = new ConditionGroup(conditionGroup, condition, relation);
                            ((ConditionNode) conditionNode).setCondition(conditionGroup);
                        }
                        for (AbstractNode grandParent : node.parents) {
                            if (grandParent.YesSon.contains(node)) {
                                grandParent.YesSon.remove(node);
                                conditionNode.addParent(grandParent, true);
                            }
                            if (grandParent.NoSon != null && grandParent.NoSon.contains(node)) {
                                grandParent.NoSon.add(conditionNode);
                                grandParent.NoSon.remove(node);
                                conditionNode.addParent(grandParent, false);
                            }
                        }
                    }
                }
                break;
            case "At Least":
                int num = getParameterAsInt(PARAMETER_NUM);
                conditionNode = new AtLeastNode();
                ((AtLeastNode) conditionNode).setNum(num);
                for (Pack temp_pack : packs) {
                    Map<AbstractNode, Boolean> current_parents = temp_pack.current_parents;
                    for (Map.Entry<AbstractNode, Boolean> entry : current_parents.entrySet()) {
                        AbstractNode node = entry.getKey();
                        step.nodes.remove(node);
                        AbstractCondition condition = node.getCondition();
                        ((AtLeastNode) conditionNode).addCondition(condition);
                        for (AbstractNode grandParent : node.parents) {
                            if (grandParent.YesSon.contains(node)) {
                                grandParent.YesSon.remove(node);
                                conditionNode.addParent(grandParent, true);
                            }
                            if (grandParent.NoSon.contains(node)) {
                                grandParent.NoSon.add(conditionNode);
                                grandParent.NoSon.remove(node);
                                conditionNode.addParent(grandParent, false);
                            }
                        }
                    }
                }
                break;
            }
        step.addNode(conditionNode);
        pack.current_parents.clear();
        pack.current_parents.put(conditionNode,true);
        OutModelOutput.deliver(pack);
    }

    @Override
    public List<ParameterType> getParameterTypes(){

        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeEnumeration(PARAMETER_LOOP, "Loop Over Elements", new ParameterTypeString(
                PARAMETER_LOOP,
                "Input Element Name"
        )));

        String[] type=new String[2];
        type[0]="Relation";
        type[1]="At Least";

        String[] relation_type =new String[2];
        relation_type[0]="and";
        relation_type[1]="or";

        ParameterTypeStringCategory opType=new ParameterTypeStringCategory(PARAMETER_TYPE, "Choose Combination Type", type);
        ParameterTypeStringCategory reType=new ParameterTypeStringCategory(PARAMETER_RE, "Choose Relation Type", relation_type);
        ParameterTypeInt numType=new ParameterTypeInt(PARAMETER_NUM,"At least () is true",1,10,2);

        reType.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "Relation"));
        numType.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "At Least"));

        types.add(opType);
        types.add(reType);
        types.add(numType);
        return types;
    }
}
