package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Exist;
import com.rapidminer.tools.LogService;

import java.util.*;
import java.util.logging.Level;

public class AtLeastNode extends AbstractNode{
    private List<AbstractCondition> conditions;

    public AtLeastNode() {
        super();
        setType("AtLeast");
        this.conditions = new java.util.ArrayList<>();
    }

    private int num;


    public String getFeature(){
        StringBuilder sb = new StringBuilder();
        for (AbstractCondition condition : conditions) {
            if (condition instanceof Compare){
                sb.append(condition.getFeature()).append(", ");
            }else if (condition instanceof ConditionGroup){
                sb.append(condition.getFeature()).append(", ");
            }else if (condition instanceof Exist){
                sb.append(condition.getFeature()).append(", ");
            }
        }
        sb.delete(sb.length()-2,sb.length());
        return sb.toString();
    }

    public String getResultName(){
        StringBuilder sb = new StringBuilder();
        sb.append("GOR").append(num).append("(");
        for (AbstractCondition condition : conditions) {
            if (condition instanceof Compare){
                sb.append(condition.getResultName()).append(", ");
            }else if (condition instanceof ConditionGroup){
                sb.append(condition.getResultName()).append(", ");
            }else if (condition instanceof Exist){
                sb.append(condition.getResultName()).append(", ");
            }
        }
        sb.delete(sb.length()-2,sb.length());
        sb.append(")");
        return sb.toString();
    }


    public Map<String,String> getOperations(){
        Map<String,String> operations = new Hashtable<>();
        for (AbstractCondition condition : conditions) {
            if (condition instanceof Compare){
                operations.put(condition.getResultName(),condition.toString());
            }else if (condition instanceof ConditionGroup){
                Map<String,String> tempOperations=((ConditionGroup) condition).getOperations();
                operations.putAll(tempOperations);
            }
        }
        return operations;
    }

    public Map<String,String> getThresholds(){
        Map<String,String> thresholds = new Hashtable<>();
        for (AbstractCondition condition : conditions) {
            if (condition instanceof Compare){
//                LogService.getRoot().log(Level.INFO,condition.getResultName());
                thresholds.put(condition.getResultName(),condition.getThreshold());
            }else if (condition instanceof ConditionGroup){
                Map<String,String> tempThresholds=((ConditionGroup) condition).getThresholds();
                thresholds.putAll(tempThresholds);
            }
        }
        return thresholds;
    }

    public List<String> getCompOpNames(){
        List<String> comp_op_names=new ArrayList<>();
        for (AbstractCondition condition : conditions) {
            if (condition instanceof Compare){
                String op="";
                if (Objects.equals(condition.getMidOperand(), "<")){
                    op="_lt";
                }else {
                    op="_gt";
                }
                comp_op_names.add(condition.getResultName()+op);
            }else if (condition instanceof ConditionGroup){
                List<String> tempCompOpNames=((ConditionGroup) condition).getCompOpNames();
                comp_op_names.addAll(tempCompOpNames);
            }
        }
        return comp_op_names;
    }

    public void addCondition(AbstractCondition condition) {
        this.conditions.add(condition);
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getImpression(){
        return null;
    }

    @Override
    public void setImpression(String impression) {
    }

    @Override
    public Set<String> getMidOutput() {
        Set<String> midOutput=new HashSet<>();
        for (AbstractCondition condition : conditions) {
            if (condition instanceof Compare) {
                midOutput.add(condition.getResultName());
            } else if (condition instanceof ConditionGroup) {
                midOutput.addAll(((ConditionGroup) condition).getMidOutput());
            }
        }
        return midOutput;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndex().toString()).append(": ");
        sb.append("{[AtLeastNode], ");
        sb.append("At least ").append(num).append(" of the conditions:\n");
        for (AbstractCondition condition : conditions) {
            sb.append(condition).append(", ").append(condition.getResultName()).append("\n");
        }
        sb.append(", [");

        for (AbstractNode parent : parents) {
            sb.append(parent.getIndex()).append(", ");
        }

        if (!parents.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]");
        sb.append(", [");
        for (AbstractNode yesSon : YesSon) {
            sb.append(yesSon.getIndex()).append(", ");
        }
        if (!YesSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append(", [");
        for (AbstractNode noSon : NoSon) {
            sb.append(noSon.getIndex()).append(", ");
        }
        if (!NoSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
