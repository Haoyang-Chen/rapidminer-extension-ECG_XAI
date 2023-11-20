package com.rapidminer.extension.ecg_xai.operator.DataOperarors;

import com.rapidminer.extension.ecg_xai.operator.Structures.StringInfo;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;

import java.util.List;

public class ResultCollectOperator extends Operator {
    private final InputPortExtender Input=new InputPortExtender("Input", getInputPorts());
    private final OutputPort Output=getOutputPorts().createPort("Output");
    private static final String PARAMETER_NAME="Name";
    public ResultCollectOperator(OperatorDescription description) {
        super(description);
        Input.start();
    }
    @Override
    public void doWork() throws com.rapidminer.operator.OperatorException {
        String name=getParameterAsString(PARAMETER_NAME);
        List<StringInfo> results= Input.getData(StringInfo.class, true);
        StringBuilder combined_res= new StringBuilder();
        if (results.get(0).toString().contains("Step")){
            for (StringInfo result : results) {
                combined_res.append(result.toString()).append("\n").append("\n");
            }
        }else {
            combined_res.append("Step:").append(name).append("\n");
            for (StringInfo result : results) {
                combined_res.append(result.toString()).append("\n");
            }
        }
        Output.deliver(new StringInfo(combined_res.toString()));
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeString(PARAMETER_NAME,"Name of the combine",true));
        return types;
    }
}
