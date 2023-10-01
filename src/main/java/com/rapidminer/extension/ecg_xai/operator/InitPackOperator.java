package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.OperatorService;

public class InitPackOperator extends Operator {
    private final OutputPort outputPort = getOutputPorts().createPort("Out pack");

    public InitPackOperator(OperatorDescription description) {
        super(description);
        getTransformer().addGenerationRule(outputPort,Pack.class);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=new Pack();
        pack.setYes();
//        Model model=pack.getModel();
//        Step step1 = new Step();
//        Compare compare=new Compare("A",">","C");
//        ConditionNode conditionNode = new ConditionNode(compare);
//        conditionNode.addParent(step1.getLast(),true);
//        step1.addNode(conditionNode);
//
//        Step step2 = new Step();
//        Compare compare2=new Compare("A",">","C");
//        ConditionNode conditionNode2 = new ConditionNode(compare2);
//        conditionNode2.addParent(step2.getLast(),true);
//        step2.addNode(conditionNode2);
//
//        model.addStep(step1);
//        model.addStep(step2);

        System.out.println(pack);
        outputPort.deliver(pack);
    }
}
