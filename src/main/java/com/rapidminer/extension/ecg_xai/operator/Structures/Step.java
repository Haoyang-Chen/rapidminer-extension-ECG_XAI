package com.rapidminer.extension.ecg_xai.operator.Structures;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ConditionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.ImpressionNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.StartNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.tools.LogService;

import java.util.*;
import java.util.logging.Level;

public class Step {
    public List<AbstractNode> nodes=new ArrayList<>();
    public String focus_leads;
    public String name;

    public Step(){
        StartNode firstNode=new StartNode();
        nodes.add(firstNode);
    }

    public Step(Step step){
        this.nodes.addAll(step.nodes);
        this.focus_leads=step.focus_leads;
        this.name=step.name;
    }

    public void flushIndex(){
        for (int i=0;i<nodes.size();i++){
            nodes.get(i).setIndex(i);
        }
    }

    public String getName(){
        StringBuilder name= new StringBuilder();
        for(char i:this.name.toCharArray()){
            if (Character.isUpperCase(i)){
                name.append(i);
            }
        }
        return name.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNode(AbstractNode node){
        nodes.add(node);
        node.setIndex(nodes.size()-1);
    }

    public AbstractNode getLast(){
        return nodes.get(nodes.size()-1);
    }

    public AbstractNode getLastCon(){
        for (int i=this.nodes.size()-1;i>=0;i--){
            AbstractNode node=this.nodes.get(i);
            if (!Objects.equals(node.getType(), "Impression")){
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Focuse On: ").append(focus_leads).append("\n");
        sb.append("[Nodes: \n");
        for (AbstractNode node : nodes) {
            sb.append(node.toString()).append(", \n");
        }
        if (!nodes.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Step step = new Step();
        Compare compare=new Compare("A",">","C");
        ConditionNode conditionNode = new ConditionNode(compare);
        conditionNode.addParent(step.getLast(),true);
        step.addNode(conditionNode);
        ImpressionNode impressionNode = new ImpressionNode("B");
        impressionNode.addParent(conditionNode,true);
        impressionNode.abnormal=false;
        step.addNode(impressionNode);
        System.out.println(step);
        System.out.println(step.getTrace());
    }

//    Step1:
//    {
//        Assess Rhythm & Rate
//        Focuse On: [II, V1]
//    [Nodes:
//        0: {[StartNode], [1]},
//        1: {[ConditionNode], SINUS is true, none, [0], [2], [10, 11]},
//        2: {[ConditionNode], RR_DIFF < 120ms, ARRH, [1], [3], [5, 6, 4]},
//        3: {[ImpressionNode], SARRH, [2]},
//        4: {[ConditionNode], HR < 60, BRAD, [2], [7], []},
//        5: {[ConditionNode], 60 < HR < 100, R, [2], [8], []},
//        6: {[ConditionNode], HR > 100, TACH, [2], [9], []},
//        7: {[ImpressionNode], SBRAD, [4]},
//        8: {[ImpressionNode], SR, [5]},
//        9: {[ImpressionNode], STACH, [6]},
//        10: {[ImpressionNode], AFLT (Abnormal), [1]},
//        11: {[ImpressionNode], AFIB (Abnormal), [1]},]
//    }
//        SINUS->ARRH->SARRH
    //    SINUS->~ARRH->BRAD->SBRAD
    //    SINUS->~ARRH->R->SR
    //    SINUS->~ARRH->TACH->STACH
//        ~SINUS->AFLT,AFIB
//    },


//    Step2:
//    {
//        Assess Intervals & Blocks
//        Focuse On: [V1, V2, V5, V6]
//[Nodes:
//    0: {[StartNode], [2, 1]},
//        1: {[ConditionNode], QRS_DUR > 120ms, LQRS, [0], [5, 4], []},
//        2: {[ConditionNode], PR_DUR > 200ms, LPR, [0], [3], []},
//        3: {[ImpressionNode], AVB (Abnormal), [2]},
//        4: {[ImpressionNode], RBBB (Abnormal), [1]},
//        5: {[ImpressionNode], LBBB (Abnormal), [1]},]
//    },
    // LQRS->RBBB
    // LQRS->LBBB
    // LPR->AVB

    public Dictionary<String,String> getOperations(){
        Dictionary<String,String> operations=new Hashtable<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Condition")){
                if (node.getCondition().type=="Compare"){
                    operations.put(node.getCondition().getResultName(),node.getCondition().toString());
                }else if (node.getCondition().type=="DoubleCompare"){
                    operations.put(node.getCondition().getResultName(),"~"+node.brothers.get(0).getCondition().getResultName()+" AND ~"+node.brothers.get(1).getCondition().getResultName());
                }
//                operations.put(node.getCondition().getResultName(),node.getCondition().toString());
            }
        }
        return operations;
    }

    public List<String> getRequiredFeatures(){
        List<String> features=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Condition")){
                features.add(node.getCondition().getFeature());
            }
        }
        return features;
    }

    public Dictionary<String,String> getThresholds(){
        Dictionary<String,String> threshold=new Hashtable<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Condition")){
                if (Objects.equals(node.getCondition().type, "Compare")) {
                    threshold.put(node.getCondition().getResultName(), node.getCondition().getRightOperand());
                }
            }
        }
//        LogService.getRoot().log(Level.INFO,threshold.toString());
        return threshold;
    }

    public String getFocusedLeads(){
        return this.focus_leads;
    }

    public List<String> getObjFeatNames(){
        List<String> obj_feat_names=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Impression")){
                obj_feat_names.add(node.getImpression()+"_"+this.getName());
            }
        }
//        LogService.getRoot().log(Level.INFO,obj_feat_names.toString());
        return obj_feat_names;
    }

    public List<String> getCompOpNames(){
        List<String> comp_op_names=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Condition")){
                if (Objects.equals(node.getCondition().type, "Compare")) {
                    String op = "";
                    if (Objects.equals(node.getCondition().getMidOperand(), "<")) {
                        op = "_lt";
                    } else {
                        op = "_gt";
                    }
                    comp_op_names.add(node.getCondition().getResultName() + op);
                }
            }
        }
//        LogService.getRoot().log(Level.INFO,comp_op_names.toString());
        return comp_op_names;
    }


    public List<String> getNormIfNot(){
        List<String> norm_if_not=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Impression")){
                LogService.getRoot().log(Level.INFO,node.toString());
                if (node.abnormal){
                    LogService.getRoot().log(Level.INFO,"abnormal");
                    norm_if_not.add(node.getImpression()+"_"+this.getName()+"_imp");
                }
            }
        }
//        LogService.getRoot().log(Level.INFO,norm_if_not.toString());
        return norm_if_not;
    }

    public String findTrace(AbstractNode node, String trace){
        if (node.getType()=="Start Node"){
            return trace;
        } else{
            AbstractNode[] parents=node.parents.toArray(new AbstractNode[node.parents.size()]);
            AbstractNode parent=parents[0];
            trace=findTrace(parent,trace);
            if (parent.getType()=="Start Node"){
                return trace;
            }else {
                if (parent.YesSon.contains(node)) {
                    trace = trace + parent.getCondition().getResultName() + "->";
                } else {
                    trace = trace + "~" + parent.getCondition().getResultName() + "->";
                }
            }
            if (node.getType()=="Impression"){
                trace=trace+node.getImpression();
            }
        }
        return trace;
    }

    public List<String> getTrace(){
        List<String> traces=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Impression")){
                traces.add(findTrace(node,""));
            }
        }
//        LogService.getRoot().log(Level.INFO,traces.toString());
        return traces;
    }
}
//focused_leads: list[str] = ALL_LEADS
//obj_feat_names: list[str] = ['LQRS_WPW', 'SPR']
//feat_imp_names: list[str] = ['LQRS_WPW_imp', 'SPR_imp']
//comp_op_names: list[str] = ['lqrs_wpw_gt', 'spr_lt']
//imply_names: list[str] = ['WPW_imply', 'IVCD_imply']
//pred_dx_names: list[tuple[str, list[str]]] = [('NORM', ['NORM_imp']), ('WPW', ['WPW_imp']), ('IVCD', ['IVCD_imp'])]
//NORM_if_NOT: list[str] = ['WPW_imp', 'IVCD_imp']
