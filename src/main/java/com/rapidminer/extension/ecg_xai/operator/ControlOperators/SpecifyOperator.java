package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.Pack;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.parameter.*;

import java.util.List;

public class SpecifyOperator extends OperatorChain {
    public final InputPort InModelInput=getInputPorts().createPort("Model");
    public final OutputPort InModelOutput = getSubprocess(0).getInnerSources().createPort("Model");
    public final InputPort YesOutModelInput=getSubprocess(0).getInnerSinks().createPort("Yes");
    public final OutputPort YesOutModelOutput=getOutputPorts().createPort("Yes");
    public final InputPort NoOutModelInput=getSubprocess(0).getInnerSinks().createPort("No");
    public final OutputPort NoOutModelOutput=getOutputPorts().createPort("No");

    public final PortPairExtender inExtender =
            new PortPairExtender("Input",
                    getInputPorts(), getSubprocess(0).getInnerSources());

    private static final String PARAMETER_LOOP="Specify";

    public SpecifyOperator(OperatorDescription description) {
        super(description, "Executed Process");
        inExtender.start();
        getTransformer().addRule(new PassThroughRule(InModelInput, InModelOutput, false));
        getTransformer().addRule(new PassThroughRule(YesOutModelInput, YesOutModelOutput, false));
        getTransformer().addRule(new PassThroughRule(NoOutModelInput, NoOutModelOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        InModelOutput.deliver(InModelInput.getData(Pack.class));
        getSubprocess(0).execute();
        YesOutModelOutput.deliver(YesOutModelInput.getData(Pack.class));
        NoOutModelOutput.deliver(NoOutModelInput.getData(Pack.class));
    }

    @Override
    public List<ParameterType> getParameterTypes(){

        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeEnumeration(PARAMETER_LOOP, "Specify", new ParameterTypeString(
                PARAMETER_LOOP,
                "Input Element Name"
        )));
        return types;
    }
}
