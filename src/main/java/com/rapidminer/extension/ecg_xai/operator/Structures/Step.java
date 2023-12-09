package com.rapidminer.extension.ecg_xai.operator.Structures;

import com.rapidminer.extension.ecg_xai.operator.nodes.*;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.ConditionGroup;

import java.util.*;

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

    public String getName() {
        StringBuilder initials = new StringBuilder();

        // Split the name into words
        String[] words = this.name.split("\\s+");

        // Extract the initial letter of each word
        for (String word : words) {
            if (!word.isEmpty()) {
                char initial = Character.toUpperCase(word.charAt(0));
                initials.append(initial);
            }
        }

        return initials.toString().replace("&","");
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

    public Map<String,String> getOperations(){
        Map<String,String> operations=new Hashtable<>();
        Map<String,String> temp=new Hashtable<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Condition")){
                if (node.getCondition().type=="Compare"){
                    operations.put(node.getCondition().getResultName(),node.getCondition().toString());
                }else if (node.getCondition().type=="DoubleCompare"){
                    operations.put(node.getCondition().getResultName(),"~"+node.brothers.get(0).getCondition().getResultName()+" AND ~"+node.brothers.get(1).getCondition().getResultName());
                }else if (node.getCondition().type=="ConditionGroup"){
                    Map<String,String> tempOperations=((ConditionGroup) node.getCondition()).getOperations();
                    operations.putAll(tempOperations);
                }
            }else if (node instanceof AtLeastNode){
                Map<String,String> tempOperations=(((AtLeastNode) node).getOperations());
                operations.putAll(tempOperations);
            }
        }
        for (String key:operations.keySet()){
            String value=operations.get(key);
            key="'"+key+"'";
            value="'"+value+"'";
            temp.put(key,value);
        }
        return temp;
    }

    public Set<String> getRequiredFeatures(){
        Set<String> features=new HashSet<>();
        Set<String> temp=new HashSet<>();
        for (AbstractNode node:nodes) {
            if (Objects.equals(node.getType(), "Condition")) {
                if (node.getCondition().type == "Compare") {
                    features.add(node.getCondition().getFeature());
                } else if (node.getCondition().type == "ConditionGroup") {
                    String tempFeatures = node.getCondition().getFeature();
                    features.addAll(List.of(tempFeatures.split(", ")));
                }
            } else if (node instanceof AtLeastNode) {
                String tempFeatures = (((AtLeastNode) node).getFeature());
                features.addAll(List.of(tempFeatures.split(", ")));
            }
        }
        for (String feature:features){
            temp.add("'" + feature + "'");
        }
        return temp;
    }

    public Map<String,String> getThresholds(){
        Map<String,String> threshold=new Hashtable<>();
        Map<String,String> temp=new Hashtable<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Condition")){
                if (Objects.equals(node.getCondition().type, "Compare")) {
                    threshold.put(node.getCondition().getResultName(), node.getCondition().getRightOperand());
                }else if (node.getCondition().type=="ConditionGroup"){
                    Map<String,String> tempThreshold=((ConditionGroup) node.getCondition()).getThresholds();
                    threshold.putAll(tempThreshold);
                }
            }else if (node instanceof AtLeastNode){
                Map<String,String> tempThreshold=(((AtLeastNode) node).getThresholds());
                threshold.putAll(tempThreshold);
            }
        }
        for (String key:threshold.keySet()){
            String value=threshold.get(key);
            key="'"+key+"'";
            value="'"+value+"'";
            temp.put(key,value);
        }
        return temp;
    }

    public String getFocusedLeads(){
        List<String> temp=new ArrayList<>();
        for (String lead:focus_leads.split(", ")){
            temp.add("'"+lead+"'");
        }
        return temp.toString();
    }

    public List<String> getResultOutput(){
        List<String> resultOutput =new ArrayList<>();
        List<String> temp=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Impression")){
                resultOutput.add(node.getImpression()+"_"+this.getName());
            }
        }
        for (String obj: resultOutput){
            temp.add("'"+obj+"'");
        }
        return temp;
    }

    public List<String> getDiagnosis() {
        List<String> results = new ArrayList<>();
        for (AbstractNode node : nodes) {
            if (node instanceof ImpressionNode) {
                results.add("'" + node.getImpression() + "'");
            }
        }
        return results;
    }

    public List<String> getCompOpNames(){
        List<String> comp_op_names=new ArrayList<>();
        List<String> temp=new ArrayList<>();
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
                }else if(node.getCondition().type=="ConditionGroup"){
                    List<String> tempCompOpNames=((ConditionGroup) node.getCondition()).getCompOpNames();
                    comp_op_names.addAll(tempCompOpNames);
                }
            }else if (node instanceof AtLeastNode){
                List<String> tempCompOpNames=(((AtLeastNode) node).getCompOpNames());
                comp_op_names.addAll(tempCompOpNames);
            }
        }
        for (String comp:comp_op_names){
            temp.add("'"+comp+"'");
        }
        return temp;
    }


    public List<String> getNormIfNot(){
        List<String> norm_if_not=new ArrayList<>();
        List<String> temp=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Impression")){
                if (node.abnormal){
                    norm_if_not.add(node.getImpression()+"_"+this.getName()+"_imp");
                }
            }
        }
        for (String norm:norm_if_not){
            temp.add("'"+norm+"'");
        }
        return temp;
    }

    public Set<String> getObjFeatNames(){
        Set<String> objFeatNames =new HashSet<>();
        Set<String> temp=new HashSet<>();
        for (AbstractNode node:nodes){
            if (!(node instanceof StartNode || node instanceof ImpressionNode)){
                objFeatNames.addAll(node.getMidOutput());
            }
        }
        for (String mid: objFeatNames){
            temp.add("'" + mid + "'");
        }
        return temp;
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
                    trace = trace + parent.getResultName() + " AND ";
                } else {
                    trace = trace + "~(" + parent.getResultName() + ") AND ";
                }
            }
            if (node.getType()=="Impression"){
                trace=trace.substring(0,trace.length()-5);
                trace+=" -> ";
                trace=trace+node.getImpression();
            }
        }
        return trace;
    }

    public List<String> getTrace(){
        List<String> traces=new ArrayList<>();
        List<String> temp=new ArrayList<>();
        for (AbstractNode node:nodes){
            if (Objects.equals(node.getType(), "Impression")){
                traces.add(findTrace(node,""));
            }
        }
        for (String trace:traces){
            temp.add("'"+trace+"'");
        }
        return temp;
    }
}
//focused_leads: list[str] = ALL_LEADS
//obj_feat_names: list[str] = ['LQRS_WPW', 'SPR']
//feat_imp_names: list[str] = ['LQRS_WPW_imp', 'SPR_imp']
//comp_op_names: list[str] = ['lqrs_wpw_gt', 'spr_lt']
//imply_names: list[str] = ['WPW_imply', 'IVCD_imply']
//pred_dx_names: list[tuple[str, list[str]]] = [('NORM', ['NORM_imp']), ('WPW', ['WPW_imp']), ('IVCD', ['IVCD_imp'])]
//NORM_if_NOT: list[str] = ['WPW_imp', 'IVCD_imp']
