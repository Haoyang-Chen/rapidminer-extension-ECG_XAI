package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;

public class StepOperator extends AbstractStepOperator {


    public StepOperator(OperatorDescription description) {
        super(description);
        inExtender.start();
        outExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
        outExtender.reset();
        inExtender.passDataThrough();
        getSubprocess(0).execute();
        OutSummaryOutput.deliver(getSubprocess(0).getAllInnerOperators().get(0).getOutputPorts().getPortByIndex(0).getData(Pack.class));
        outExtender.collect();
    }
}
