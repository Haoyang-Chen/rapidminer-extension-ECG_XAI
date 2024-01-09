package com.rapidminer.extension.ecg_xai.operator.DataOperarors;

import com.rapidminer.extension.ecg_xai.operator.Structures.*;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeEnumeration;
import com.rapidminer.parameter.ParameterTypeStringCategory;
import com.rapidminer.tools.LogService;
import org.apache.tools.ant.taskdefs.Tar;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class ResultOperator2 extends Operator {
    private final InputPort Input=getInputPorts().createPort("In pack");
    private final OutputPort output=getOutputPorts().createPort("Out result");
    private static final String PARAMETER_NAME="Result Name";
    private static final String PARAMETER_TYPE="Result Type";
    private static final String PARAMETER_AB_TYPE ="Abnormality";
    public ResultOperator2(OperatorDescription description) {
        super(description);
    }

    private int findSame(ImpressionNode Target_node, Step step){
        for (AbstractNode node:step.nodes){
            if (Objects.equals(node.getImpression(), Target_node.getImpression())){
                if (node!=Target_node){
                    if (node instanceof ImpressionNode){
                        return step.nodes.indexOf(node);
                    }
                }
            }
        }
        return -1;
    }
    private void rename(ImpressionNode Target_node, Step step){
        int index=0;
        String name=Target_node.getImpression();
        int i=findSame(Target_node,step);
//        LogService.getRoot().log(Level.INFO, i+"");
        while (i>=0){
            Target_node.setImpression(name+"_"+Target_node.parents.stream().findFirst().get().getResultName().substring(0,index));
            AbstractNode node=step.nodes.get(i);
            node.setImpression(name+"_"+node.parents.stream().findFirst().get().getResultName().substring(0,index));
            i=findSame(Target_node,step);
            index+=1;
//            LogService.getRoot().log(Level.INFO,"Rename "+Target_node.getImpression());
        }
    }

    @Override
    public void doWork() throws com.rapidminer.operator.OperatorException {
        String name=getParameterAsString(PARAMETER_NAME);
        String type=getParameterAsString(PARAMETER_TYPE);
        boolean ab_type =getParameterAsBoolean(PARAMETER_AB_TYPE);
        Pack pack=Input.getData(Pack.class);
//        Model model=pack.getModel();
//        Step step=model.getLastStep();
        Step step=pack.getStep();
        ImpressionNode impNode;
        name+="_"+step.getName();
        if (ab_type) {
            impNode = new ImpressionNode(name);
            impNode.abnormal=true;
        }else{
            impNode = new ImpressionNode(name);
            impNode.abnormal=false;
        }
        for (Map.Entry<AbstractNode, Boolean> entry : pack.current_parents.entrySet()){
            AbstractNode parent=entry.getKey();
            Boolean nodeYes=entry.getValue();
            impNode.addParent(parent,nodeYes);
        }
        step.addNode(impNode);
        rename(impNode,step);

        this.rename(name);
        if (Objects.equals(type, "General")) {
            output.deliver(new StringInfo_General(name));
        }else{
            output.deliver(new StringInfo_Lead(name));
        }
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        ImpressionName impressionName=new ImpressionName();
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeEnumeration(PARAMETER_NAME, "The list of results", new ParameterTypeStringCategory(
                PARAMETER_NAME,
                "Choose Result Name",
                impressionName.getImpressions()
        )));
        types.add(new ParameterTypeStringCategory(PARAMETER_TYPE,"Type of the Data",new String[]{"General","Lead Specific"},"General"));
        types.add(new ParameterTypeBoolean(PARAMETER_AB_TYPE,"Abnormality",false));
        return types;
    }
}
