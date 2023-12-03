package com.rapidminer.extension.ecg_xai.operator.nodes.condition;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;

import java.util.*;

public class ConditionGroup extends AbstractCondition{
    private AbstractCondition left;
    private AbstractCondition right;
    private String relation;

    public ConditionGroup(){
        super();
        this.type="ConditionGroup";
        left=null;
        right=null;
        relation=null;
    }
    public ConditionGroup(AbstractCondition left, AbstractCondition right, String operator) {
        super();
        this.type="ConditionGroup";
        this.left = left;
        this.right = right;
        this.relation = operator;
    }

    public void setLeft(AbstractCondition left) {
        this.left = left;
    }

    public void setRight(AbstractCondition right) {
        this.right = right;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMidOperand() {
        return relation;
    }

    public AbstractCondition getLeft() {
        return left;
    }

    public AbstractCondition getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + relation + " " + right + ")";
    }

    @Override
    public String getResultName(){
        if (!Objects.equals(this.resultName, "none") && !this.resultName.equals("None")){
            if (this.getLead()!=null){
                return this.resultName+"_"+this.getLead();
            }else {
                return this.resultName;
            }
        }
        return left.getResultName()+" "+relation+" "+right.getResultName();
    }

    public Dictionary<String,String> getOperations(){
        Dictionary<String,String> operations=new Hashtable<>();
        if (left instanceof Compare){
            operations.put(left.getResultName(),left.toString());
        }else if (left instanceof ConditionGroup){
            Dictionary<String,String> tempOperations=((ConditionGroup) left).getOperations();
            for (int i=0;i<tempOperations.size();i++){
                operations.put(tempOperations.keys().nextElement(),tempOperations.elements().nextElement());
            }
        }
        if (right instanceof Compare) {
            operations.put(right.getResultName(), right.toString());
        }else if (right instanceof ConditionGroup){
            Dictionary<String,String> tempOperations=((ConditionGroup) right).getOperations();
            for (int i=0;i<tempOperations.size();i++){
                operations.put(tempOperations.keys().nextElement(),tempOperations.elements().nextElement());
            }
        }
        return operations;
    }

    public Dictionary<String,String> getThresholds(){
        Dictionary<String,String> thresholds=new Hashtable<>();
        if (left instanceof Compare){
            thresholds.put(left.getResultName(), left.getThreshold());
        }else if (left instanceof ConditionGroup){
            Dictionary<String,String> tempThresholds=((ConditionGroup) left).getThresholds();
            for (int i=0;i<tempThresholds.size();i++){
                thresholds.put(tempThresholds.keys().nextElement(),tempThresholds.elements().nextElement());
            }
        }
        if (right instanceof Compare) {
            thresholds.put(right.getResultName(), right.getThreshold());
        }else if (right instanceof ConditionGroup){
            Dictionary<String,String> tempThresholds=((ConditionGroup) right).getThresholds();
            for (int i=0;i<tempThresholds.size();i++){
                thresholds.put(tempThresholds.keys().nextElement(),tempThresholds.elements().nextElement());
            }
        }
        return thresholds;
    }

    public List<String> getCompOpNames(){
        List<String> comp_op_names=new ArrayList<>();
        if (left instanceof Compare){
            String op="";
            if (Objects.equals(left.getMidOperand(), "<")){
                op="_lt";
            }else {
                op="_gt";
            }
            comp_op_names.add(left.getResultName()+op);
        }else if (left instanceof ConditionGroup){
            List<String> tempCompOpNames=((ConditionGroup) left).getCompOpNames();
            comp_op_names.addAll(tempCompOpNames);
        }
        if (right instanceof Compare){
            String op="";
            if (Objects.equals(right.getMidOperand(), "<")){
                op="_lt";
            }else {
                op="_gt";
            }
            comp_op_names.add(right.getResultName()+op);
        }else if (right instanceof ConditionGroup){
            List<String> tempCompOpNames=((ConditionGroup) right).getCompOpNames();
            comp_op_names.addAll(tempCompOpNames);
        }
        return comp_op_names;
    }

    public String getLead(){
        if (left.getLead()!=null && right.getLead()!=null && Objects.equals(left.getLead(), right.getLead())){
            return left.getLead();
        }
        return null;
    }

    @Override
    public String getFeature() {
        return left.getFeature()+", "+right.getFeature();
    }

    @Override
    public String getOperator() {
        return null;
    }

    @Override
    public String getThreshold() {
        return null;
    }

    public String getLeftOperand() {
        return null;
    }

    public String getRightOperand() {
        return null;
    }

    public static void main(String[] args) {
        Compare condition1 = new Compare("A", ">", "B");
        Compare condition2 = new Compare("C", "<", "D");
        ConditionGroup group = new ConditionGroup(condition1, condition2, "AND");
        System.out.println(group);
    }
}
