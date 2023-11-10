package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.CollectingPortPairExtender;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.operator.ports.metadata.PassThroughRule;

public class AbstractStepOperator extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    public final OutputPort OutModelOutput=getOutputPorts().createPort("Model");
    public final PortPairExtender inExtender =
            new PortPairExtender("other",
                    getInputPorts(), getSubprocess(0).getInnerSources());
    public final CollectingPortPairExtender outExtender =
            new CollectingPortPairExtender("other",
                    getSubprocess(0).getInnerSinks(), getOutputPorts());

    public AbstractStepOperator(OperatorDescription description) {
        super(description, "Executed Process");
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(InModelInput, OutModelOutput, false));
    }

}
