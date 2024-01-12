package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.CollectingPortPairExtender;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;

public class AbstractStepOperator extends OperatorChain {
    public final InputPort InControlInput =getInputPorts().createPort("Control");
    public final OutputPort OutControlOutput=getOutputPorts().createPort("Control");
    public final OutputPort OutSummaryOutput =getOutputPorts().createPort("Summary");
    public final PortPairExtender inExtender =
            new PortPairExtender("other",
                    getInputPorts(), getSubprocess(0).getInnerSources());
    public final CollectingPortPairExtender outExtender =
            new CollectingPortPairExtender("other",
                    getSubprocess(0).getInnerSinks(), getOutputPorts());

    public AbstractStepOperator(OperatorDescription description) {
        super(description, "Executed Process");
        getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
//        getTransformer().addRule(new PassThroughRule(InControlInput, InControlOutput, false));
//        getTransformer().addRule(new PassThroughRule(InControlInput, OutSummaryOutput, false));
//        getTransformer().addRule(new PassThroughRule(InControlInput, OutControlOutput, false));
    }

}
