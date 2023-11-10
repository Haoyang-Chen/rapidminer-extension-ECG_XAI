package com.rapidminer.extension.ecg_xai.operator.names;

import java.util.*;

public class FeatureName {
    public static Set<String> FeatureList= new LinkedHashSet<>();
    static {
        FeatureList.add("SINUS");
        FeatureList.add("HR");
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

    public String[] getFeatures() {
        return FeatureList.toArray(String[]::new);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FeatureName [");
        for (String feature : FeatureList) {
            sb.append(feature).append(", ");
        }
        if (FeatureList.size() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        FeatureName featureName = new FeatureName();
        System.out.println(featureName);
    }
}
