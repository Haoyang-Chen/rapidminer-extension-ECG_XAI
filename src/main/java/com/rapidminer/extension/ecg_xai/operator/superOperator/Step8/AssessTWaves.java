package com.rapidminer.extension.ecg_xai.operator.superOperator.Step8;

import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.AtLeastNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;
import com.rapidminer.extension.ecg_xai.operator.superOperator.AbstractStepOperator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.parameter.conditions.EqualStringCondition;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rapidminer.parameter.ParameterTypeCheckBoxGroup.stringToSelection;

public class AssessTWaves extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    public final InputPort OutModelInput = getSubprocess(0).getInnerSinks().createPort("Model");
    public final OutputPort OutModelOutput=getOutputPorts().createPort("Model");
    private final InputPort T_AMPOutput=getInputPorts().createPort("T_AMP T wave amplitude");
    private final OutputPort T_AMPInput = getSubprocess(0).getInnerSources().createPort("T_AMP T wave amplitude");
    private final InputPort STEOutput=getInputPorts().createPort("STE ST elevation");
    private final OutputPort STEInput = getSubprocess(0).getInnerSources().createPort("STE ST elevation");
    private final InputPort STDOutput=getInputPorts().createPort("STD ST depression");
    private final OutputPort STDInput = getSubprocess(0).getInnerSources().createPort("STD ST depression");



    private static final String PARAMETER_ITERATE="Iterate on Leads";
    private static final String PARAMETER_LEADS="Select Leads";

    private static final String PARAMETER_TYPE="Type";
    private static final String PARAMETER_NUM="At Least () Satisfied";
    private static final String PARAMETER_RE="Relation";

    public AssessTWaves(OperatorDescription description) {
        super(description, "Executed Process");
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(InModelInput, OutModelOutput, false));
        getTransformer().addRule(new PassThroughRule(T_AMPOutput,T_AMPInput, false));
        getTransformer().addRule(new PassThroughRule(STEOutput, STEInput, false));
        getTransformer().addRule(new PassThroughRule(STDOutput, STDInput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        boolean iterate=getParameterAsBoolean(PARAMETER_ITERATE);
        if (iterate){
            String[] leads=stringToSelection(getParameterAsString(PARAMETER_LEADS));
            String type=getParameterAsString(PARAMETER_TYPE);
            List<Pack> packs=new ArrayList<>();
            for(String lead:leads){
                String T_AMP=T_AMPOutput.getData(StringInfo.class).toString()+"_"+lead;
                String STE=STEOutput.getData(StringInfo.class).toString()+"_"+lead;
                String STD=STDOutput.getData(StringInfo.class).toString()+"_"+lead;
                T_AMPInput.deliver(new StringInfo(T_AMP));
                STEInput.deliver(new StringInfo(STE));
                STDInput.deliver(new StringInfo(STD));
                getSubprocess(0).execute();
                packs.add(OutModelInput.getData(Pack.class));
            }
            Pack pack= new Pack(packs.get(0));
            Model model=pack.getModel();
            Step step=model.getLastStep();

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

        }else {
            T_AMPInput.deliver(T_AMPOutput.getData(StringInfo.class));
            STEInput.deliver(STEOutput.getData(StringInfo.class));
            STDInput.deliver(STDOutput.getData(StringInfo.class));
            getSubprocess(0).execute();
            OutModelOutput.deliver(InModelInput.getData(Pack.class));
        }
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        LeadName leadName = new LeadName();
        List<ParameterType> types = super.getParameterTypes();
        ParameterTypeCheckBoxGroup leads=new ParameterTypeCheckBoxGroup(PARAMETER_LEADS,"select the leads to iterate on");
        for (String lead: leadName.getLead()){
            leads.add("leads",lead);
        }
        ParameterTypeBoolean iterate=new ParameterTypeBoolean(PARAMETER_ITERATE,"iterate on leads",false);
        leads.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_ITERATE,true,true));
        types.add(iterate);
        types.add(leads);

        String[] type=new String[2];
        type[0]="Relation";
        type[1]="At Least";

        String[] relation_type =new String[2];
        relation_type[0]="and";
        relation_type[1]="or";

        ParameterTypeStringCategory opType=new ParameterTypeStringCategory(PARAMETER_TYPE, "Choose Combination Type", type);
        ParameterTypeStringCategory reType=new ParameterTypeStringCategory(PARAMETER_RE, "Choose Relation Type", relation_type);
        ParameterTypeInt numType=new ParameterTypeInt(PARAMETER_NUM,"At least () is true",1,10,2);

        opType.registerDependencyCondition(new BooleanParameterCondition(this,PARAMETER_ITERATE,false,true));
        reType.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "Relation"));
        numType.registerDependencyCondition(new EqualStringCondition(this, PARAMETER_TYPE, true, "At Least"));

        types.add(opType);
        types.add(reType);
        types.add(numType);
        return types;
    }
}
