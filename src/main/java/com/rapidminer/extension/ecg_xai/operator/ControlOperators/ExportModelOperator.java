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
        Attribute[] attributes = new Attribute[3];

        attributes[0] = AttributeFactory.createAttribute ("Operations", Ontology.STRING);
        attributes[1] = AttributeFactory.createAttribute("Required Features", Ontology.STRING);
        attributes[2] = AttributeFactory.createAttribute("Thresholds", Ontology.STRING);

        MemoryExampleTable table = new MemoryExampleTable(attributes);

        DataRowFactory ROW_FACTORY = new DataRowFactory(0);
        String[] data = new String[3];
        data[0]=step.getOperations().toString();
        data[1]=step.getRequiredFeatures().toString();
        data[2]=step.getThresholds().toString();

        DataRow row = ROW_FACTORY.create(data, attributes);
        table.addDataRow(row);
        ExampleSet exampleSet = table.createExampleSet();

        LogService.getRoot().log(Level.INFO,"abaaba");
        modelOutput.deliver(exampleSet);
    }
}
