package com.rapidminer.extension.ecg_xai.operator.ControlOperators;

import com.rapidminer.adaption.belt.IOTable;
import com.rapidminer.belt.column.Column;
import com.rapidminer.belt.table.Table;
import com.rapidminer.belt.table.TableBuilder;
import com.rapidminer.extension.ecg_xai.operator.Structures.Model;
import com.rapidminer.extension.ecg_xai.operator.Structures.Pack;
import com.rapidminer.extension.ecg_xai.operator.Structures.Step;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import com.rapidminer.example.*;
import com.rapidminer.example.table .*;
import com.rapidminer.example.set .*;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools .Ontology;
import java.util.*;

import java.util.UUID;
import java.util.logging.Level;

import com.rapidminer.adaption.belt.IOTable;
import com.rapidminer.belt.table.Builders;
import com.rapidminer.belt.table.Table;
import com.rapidminer.belt.table.TableBuilder;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.belt.BeltTools;

public class ExportModelOperator extends Operator {
    private final InputPort pacInput=getInputPorts().createPort("In pack");
    private final OutputPort modelOutput=getOutputPorts().createPort("model");
    public ExportModelOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Pack pack=pacInput.getData(Pack.class);
        Model model=pack.getModel();
        Step step=model.steps.get(1);
//        LogService.getRoot().log(Level.INFO,step.toString());
//        step.getOperations();

        // create attribute list
        Attribute[] attributes = new Attribute[10];

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


        MemoryExampleTable table = new MemoryExampleTable(attributes);

//        for (Step step:model.steps) {
            DataRowFactory ROW_FACTORY = new DataRowFactory(0);
            String[] data = new String[10];

            data[0] = step.getName();
            data[1] = step.getFocusedLeads();
            data[2] = step.getObjFeatNames().toString();
            data[3] = step.getThresholds().toString();
            data[4] = step.getCompOpNames().toString();
            data[5] = step.getNormIfNot().toString();
            data[6] = step.getTrace().toString();
            data[7]=step.getOperations().toString();
            data[8]=step.getRequiredFeatures().toString();

            DataRow row = ROW_FACTORY.create(data, attributes);
            table.addDataRow(row);
//        }
        DataRowFactory ROW_FACTORY2 = new DataRowFactory(0);
        String[] data2 = new String[10];

        data2[0] = "Summary";
        data2[9] = model.getResults().toString();

        DataRow row2 = ROW_FACTORY2.create(data2, attributes);
        table.addDataRow(row2);

        ExampleSet exampleSet = table.createExampleSet();

        LogService.getRoot().log(Level.INFO,"abaaba");
        modelOutput.deliver(exampleSet);
    }
}
