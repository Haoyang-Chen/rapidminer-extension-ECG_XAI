package com.rapidminer.extension.ecg_xai.operator.DataOperarors;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo_General;
import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.StringInfo_Lead;
import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.ParameterTypeStringCategory;

import java.util.List;

public class DataRetriever extends Operator {
    private final OutputPort DataOutput = getOutputPorts().createPort("Data");
    public static final String PARAMETER_NAME="Data Name";
    public static final String PARAMETER_TYPE="Data Type";

    public DataRetriever(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String type=getParameterAsString(PARAMETER_TYPE);
        String name=getParameterAsString(PARAMETER_NAME);
        this.rename(name);
        if (type.equals("General")){
            StringInfo_General data=new StringInfo_General(name);
            DataOutput.deliver(data);
        }else {
            StringInfo_Lead data=new StringInfo_Lead(name);
            DataOutput.deliver(data);
        }
    }
    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeString(PARAMETER_NAME,"Name of the Data",true));
        types.add(new ParameterTypeStringCategory(PARAMETER_TYPE,"Type of the Data",new String[]{"General","Lead Specific"}));
        return types;
    }
}
