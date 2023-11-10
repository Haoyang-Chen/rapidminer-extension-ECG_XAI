package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.CollectingPortPairExtender;
import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeString;

import java.util.List;

public class FeatureSourceOperator extends OperatorChain {
    public FeatureSourceOperator(OperatorDescription description) {
        super(description, "Executed Process");
        inputPortPairExtender.start();
        outExtender.start();

        getTransformer().addRule(inputPortPairExtender.makePassThroughRule());
        getTransformer().addRule(outExtender.makePassThroughRule());
    }

    private final PortPairExtender inputPortPairExtender =
            new PortPairExtender("input", getInputPorts(), getSubprocess(0).getInnerSources());

    private final CollectingPortPairExtender outExtender =
            new CollectingPortPairExtender("output",
                    getSubprocess(0).getInnerSinks(), getOutputPorts());


    @Override
    public void doWork() throws OperatorException {

        outExtender.reset();
        inputPortPairExtender.passDataThrough();

        outExtender.collect();
    }

}
