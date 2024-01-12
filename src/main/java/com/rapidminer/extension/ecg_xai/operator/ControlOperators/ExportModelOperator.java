package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;

import com.rapidminer.example.*;
import com.rapidminer.example.table .*;
import com.rapidminer.tools .Ontology;

import java.util.List;

public class ExportModelOperator extends Operator {
//    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final InputPortExtender inputExtender = new InputPortExtender("Summary", getInputPorts());
    private final OutputPort modelOutput=getOutputPorts().createPort("Summary of the Model(Table)");
    public ExportModelOperator(OperatorDescription description) {
        super(description);
        inputExtender.start();
    }

    @Override
    public void doWork() throws OperatorException {
//        Pack pack=pacInput.getData(Pack.class);
//        Model model=pack.getModel();
//        Step step=model.steps.get(1);
//        LogService.getRoot().log(Level.INFO,step.toString());
//        step.getOperations();

        // create attribute list
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

        List<Pack> packs=inputExtender.getData(Pack.class,true);

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
        modelOutput.deliver(exampleSet);
    }
}
