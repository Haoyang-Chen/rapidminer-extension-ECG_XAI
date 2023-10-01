package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.OperatorService;

import java.util.logging.Level;

public class StepStartOpeartor extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort pacOutput=getOutputPorts().createPort("Out pack");

    public StepStartOpeartor(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
        Model model=pack.getModel();
        Step step=new Step();
        model.addStep(step);

        pacOutput.deliver(pack);
    }
}
