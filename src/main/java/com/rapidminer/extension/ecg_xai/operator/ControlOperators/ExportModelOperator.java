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
        Attribute[] attributes = new Attribute[8];

        attributes[0] = AttributeFactory.createAttribute ("focused leads", Ontology.STRING);
        attributes[1] = AttributeFactory.createAttribute("obj_feat_names", Ontology.STRING);
        attributes[2] = AttributeFactory.createAttribute("thresholds", Ontology.STRING);
        attributes[3] = AttributeFactory.createAttribute("comp_op_names", Ontology.STRING);
        attributes[4] = AttributeFactory.createAttribute("NORM_if_NOT", Ontology.STRING);
        attributes[5] = AttributeFactory.createAttribute("traces", Ontology.STRING);
        attributes[6] = AttributeFactory.createAttribute ("Operations", Ontology.STRING);
        attributes[7] = AttributeFactory.createAttribute("Required Features", Ontology.STRING);


        MemoryExampleTable table = new MemoryExampleTable(attributes);

//        for (Step step:model.steps) {
            DataRowFactory ROW_FACTORY = new DataRowFactory(0);
            String[] data = new String[8];

            data[0] = step.getFocusedLeads();
            data[1] = step.getObjFeatNames().toString();
            data[2] = step.getThresholds().toString();
            data[3] = step.getCompOpNames().toString();
            data[4] = step.getNormIfNot().toString();
            data[5] = step.getTrace().toString();
            data[6]=step.getOperations().toString();
            data[7]=step.getRequiredFeatures().toString();

            DataRow row = ROW_FACTORY.create(data, attributes);
            table.addDataRow(row);
//        }
        ExampleSet exampleSet = table.createExampleSet();

        LogService.getRoot().log(Level.INFO,"abaaba");
        modelOutput.deliver(exampleSet);
    }
}
