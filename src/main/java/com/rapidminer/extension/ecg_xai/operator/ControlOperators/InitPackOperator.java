package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;

public class InitPackOperator extends Operator {
//    private final OutputPortExtender outputPortExtender = new OutputPortExtender("Out pack", getOutputPorts());
    private final OutputPort outputPort = getOutputPorts().createPort("Model");
    public InitPackOperator(OperatorDescription description) {
        super(description);
//        outputPortExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=new Pack();
        outputPort.deliver(pack);
//        for (OutputPort outputPort : outputPortExtender.getManagedPorts()) {
//            outputPort.deliver(new Pack(pack));
//        }
    }
}
