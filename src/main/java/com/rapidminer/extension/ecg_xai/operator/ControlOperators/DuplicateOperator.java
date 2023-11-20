package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.OutputPortExtender;

public class DuplicateOperator extends Operator {
    private final InputPort inputPort = getInputPorts().createPort("input");
    private final OutputPortExtender outputExtender = new OutputPortExtender("output", getOutputPorts());
    public DuplicateOperator(OperatorDescription description) {
        super(description);
        outputExtender.start();
    }

    @Override
    public void doWork() throws UserError {
        Pack input = inputPort.getData(Pack.class);
        if (input != null) {
            for (OutputPort outputPort : outputExtender.getManagedPorts()) {
                outputPort.deliver(new Pack(input));
            }
        }
    }
}
