package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.OperatorService;

import java.util.List;

public class CombineOperator extends Operator {
    private final InputPortExtender inputPortExtender = new InputPortExtender("Input", getInputPorts());
    private final OutputPort outputPort = getOutputPorts().createPort("Output");
    public CombineOperator(OperatorDescription description) {
        super(description);
        inputPortExtender.ensureMinimumNumberOfPorts(1);
        inputPortExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
        List<Pack> packs = inputPortExtender.getData(Pack.class, true);
        Pack pack = packs.get(0);
        
    }
}
