package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.adaption.belt.IOTable;
import com.rapidminer.belt.column.ColumnType;
import com.rapidminer.belt.table.Builders;
import com.rapidminer.belt.table.Table;
import com.rapidminer.belt.table.TableBuilder;
import com.rapidminer.operator.IOTableModel;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MDInteger;
import com.rapidminer.operator.ports.metadata.SetRelation;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.operator.ports.metadata.TableModelMetaData;
import com.rapidminer.operator.ports.metadata.table.TableMetaData;
import com.rapidminer.operator.ports.metadata.table.TableMetaDataBuilder;
import com.rapidminer.operator.ports.metadata.table.TablePassThroughRule;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.tools.belt.BeltTools;

import java.util.List;
import java.util.logging.Level;


public class MyOwnOperator extends Operator {

    private InputPort tableInput = getInputPorts().createPort("example set");
    private OutputPort tableOutput = getOutputPorts().createPort("example set");
    public static final String PARAMETER_TEXT = "log text";
    public static final String PARAMETER_USE_CUSTOM_TEXT = "use custom text";

    public MyOwnOperator(OperatorDescription description) {
        super(description);
        tableInput.addPrecondition(
                new SimplePrecondition(tableInput, new TableMetaData()));
        getTransformer().addRule(
                new TablePassThroughRule(tableInput,tableOutput, SetRelation.EQUAL){
                    @Override
                    public TableMetaData modifyTableMetaData(TableMetaData metaData){
                        TableMetaDataBuilder builder = new TableMetaDataBuilder(metaData);
                        builder.add("newAttribute", ColumnType.REAL, new MDInteger(0));
                        return metaData;
                    }
        });
    }

    @Override
    public void doWork() throws OperatorException{
        String text= getParameterAsString(PARAMETER_TEXT);
        LogService.getRoot().log(Level.INFO,"doing Nothing..."+text);

        IOTable ioTable=tableInput.getData(IOTable.class);
        Table table= ioTable.getTable();
        TableBuilder builder= Builders.newTableBuilder(table);
        builder.addReal("newAttribute",i->Math.random()*10);
        Table newTable = builder.build(BeltTools.getContext(this));
        IOTable newIOTable=new IOTable(newTable);
        newIOTable.getAnnotations().addAll(ioTable.getAnnotations());
        tableOutput.deliver(newIOTable);
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        List<ParameterType> types=super.getParameterTypes();

        types.add(new ParameterTypeBoolean(
                PARAMETER_USE_CUSTOM_TEXT,
                "if checked, use custon text",
                false,
                false
        ));

        ParameterType type=new ParameterTypeString(
                PARAMETER_TEXT,
                "This parameter defines hich text is logged to the console when this operator is executed.",
                "This is a default text",
                false
        );

        type.registerDependencyCondition(
                new BooleanParameterCondition(
                        this, PARAMETER_USE_CUSTOM_TEXT,true,true));

        types.add(type);

        return types;
    }
}
