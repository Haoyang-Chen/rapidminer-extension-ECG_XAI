package com.rapidminer.extension.ecg_xai.operator;

import java.util.HashSet;
import java.util.Set;

public class FeatureName {
    public static Set<String> FeatureList=new HashSet<>();
    static {
        FeatureList.add("HR");
        FeatureList.add("SINUS");
        FeatureList.add("RR_DIFF");
        FeatureList.add("PR_DUR");
        FeatureList.add("QRS_DUR");
        FeatureList.add("ST_AMP");
        FeatureList.add("Q_DUR");
        FeatureList.add("Q_AMP");
        FeatureList.add("P_DUR");
        FeatureList.add("P_AMP");
        FeatureList.add("AGE");
        FeatureList.add("MALE");
        FeatureList.add("R_AMP");
        FeatureList.add("S_AMP");
        FeatureList.add("RS_RATIO");
        FeatureList.add("T_AMP");
        FeatureList.add("QRS_SUM");
    }
}
