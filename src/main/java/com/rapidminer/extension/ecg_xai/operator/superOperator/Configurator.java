package com.rapidminer.extension.ecg_xai.operator.superOperator;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.extension.ecg_xai.operator.ControlOperators.StepStartOpeartor;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;

import java.util.ArrayList;
import java.util.List;

public class Configurator extends OperatorChain {
    public final OutputPort OutConfigOutput =getOutputPorts().createPort("Config");

    public Configurator(OperatorDescription description) {
        super(description, "Executed Process");
    }

    @Override
    public void doWork() throws OperatorException {
        getSubprocess(0).execute();
        List<Operator> steps=getSubprocess(0).getAllInnerOperators();
        List<Pack> packs =new ArrayList<>();
        for (Operator step:steps) {
            if (step instanceof StepStartOpeartor){
                packs.add(step.getOutputPorts().getPortByIndex(0).getData(Pack.class));
            }
        }

        Attribute[] attributes = new Attribute[11];

        attributes[0] = AttributeFactory.createAttribute ("Name", Ontology.STRING);
        attributes[1] = AttributeFactory.createAttribute ("focused leads", Ontology.STRING);
        attributes[2] = AttributeFactory.createAttribute("obj_feat_names", Ontology.STRING);
        attributes[3] = AttributeFactory.createAttribute("thresholds", Ontology.STRING);
        attributes[4] = AttributeFactory.createAttribute("comp_op_names", Ontology.STRING);
        attributes[5] = AttributeFactory.createAttribute("NORM_if_NOT", Ontology.STRING);
        attributes[6] = AttributeFactory.createAttribute("traces", Ontology.STRING);
        attributes[7] = AttributeFactory.createAttribute ("Operations", Ontology.STRING);
        attributes[8] = AttributeFactory.createAttribute("Required Features", Ontology.STRING);
        attributes[9] = AttributeFactory.createAttribute("diagnosis", Ontology.STRING);
        attributes[10] = AttributeFactory.createAttribute("ResultOutputs", Ontology.STRING);


        MemoryExampleTable table = new MemoryExampleTable(attributes);

        for (Pack pack:packs) {
            Step step = pack.getStep();
            DataRowFactory ROW_FACTORY = new DataRowFactory(0);
            String[] data = new String[11];

            data[0] = step.getName();
            data[1] = step.getFocusedLeads();
            data[2] = step.getObjFeatNames().toString();
            data[3] = step.getThresholds().toString().replace("=",":");
            data[4] = step.getCompOpNames().toString();
            data[5] = step.getNormIfNot().toString();
            data[6] = step.getTrace().toString();
            data[7]=step.getOperations().toString().replace("=",":");
            data[8]=step.getRequiredFeatures().toString();
            data[9]=step.getDiagnosis().toString();
            data[10]=step.getResultOutput().toString();

            DataRow row = ROW_FACTORY.create(data, attributes);
            table.addDataRow(row);
        }

        ExampleSet exampleSet = table.createExampleSet();

//        LogService.getRoot().log(Level.INFO,"abaaba");
        OutConfigOutput.deliver(exampleSet);
    }
}
