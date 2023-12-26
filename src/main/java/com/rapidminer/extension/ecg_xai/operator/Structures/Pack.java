package com.rapidminer.extension.ecg_xai.operator.Structures;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObjectAdapter;

import java.util.HashMap;
import java.util.Map;

public class Pack extends ResultObjectAdapter {
//    public Model model=new Model();
    public Step step=new Step();
    public static FeatureName feature=new FeatureName();
    public static ImpressionName impression=new ImpressionName();
    public static LeadName lead=new LeadName();
//    public Boolean yes;
//    public List<AbstractNode> current_parents=new ArrayList<>();
//    public List<Boolean> parents_yes=new ArrayList<>();
    public Map<AbstractNode,Boolean> current_parents= new HashMap<>();

    public Pack(){

    }

    @Override
    public IOObject copy() {
        return new Pack(this);
    }

    public Pack(Pack pack){
//        this.model=new Model(pack.getModel());
//        this.model=pack.getModel();
        this.step=pack.step;
//        this.yes=true;
        this.current_parents=new HashMap<>();
        this.current_parents.putAll(pack.current_parents);
    }

//    public void setYes(){
//        yes=true;
//    }
//
//    public void setNo(){
//        yes=false;
//    }

//    public Model getModel(){
//        return model;
//    }

    public Step getStep(){
        return step;
    }
    public FeatureName getFeature(){
        return feature;
    }

    public ImpressionName getImpression(){
        return impression;
    }

    public LeadName getLead(){
        return lead;
    }

//    @Override
//    public String toString() {
//        return model.toString();
//    }
    @Override
    public String toString() {
        return step.toString();
    }
}
