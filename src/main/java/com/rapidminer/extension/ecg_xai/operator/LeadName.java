package com.rapidminer.extension.ecg_xai.operator;

import java.util.HashSet;
import java.util.Set;

public class LeadName {
    public static Set<String> LeadList=new HashSet<>();
    static {
        LeadList.add("I");
        LeadList.add("II");
        LeadList.add("III");
        LeadList.add("aVR");
        LeadList.add("aVL");
        LeadList.add("aVF");
        LeadList.add("V1");
        LeadList.add("V2");
        LeadList.add("V3");
        LeadList.add("V4");
        LeadList.add("V5");
        LeadList.add("V6");
    }
}
