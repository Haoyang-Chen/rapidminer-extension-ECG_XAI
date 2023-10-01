package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.names.FeatureName;
import com.rapidminer.extension.ecg_xai.operator.names.ImpressionName;
import com.rapidminer.extension.ecg_xai.operator.names.LeadName;
import com.rapidminer.operator.ResultObjectAdapter;

public class Pack extends ResultObjectAdapter {
    public Model model=new Model();
    public static FeatureName feature=new FeatureName();
    public static ImpressionName impression=new ImpressionName();
    public static LeadName lead=new LeadName();

    public Model getModel(){
        return model;
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

    @Override
    public String toString() {
        return model.toString();
    }
}
