package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.OutputPortExtender;
import com.rapidminer.tools.OperatorService;

public class InitPackOperator extends Operator {
    private final OutputPortExtender outputPortExtender = new OutputPortExtender("Out pack", getOutputPorts());

    public InitPackOperator(OperatorDescription description) {
        super(description);
        outputPortExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=new Pack();

        for (OutputPort outputPort : outputPortExtender.getManagedPorts()) {
            outputPort.deliver(new Pack(pack));
        }
    }
}
