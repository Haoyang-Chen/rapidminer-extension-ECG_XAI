package com.rapidminer.extension.ecg_xai.operator.names;

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

    public String[] getLead() {
        return LeadList.toArray(String[]::new);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LeadName [");
        for (String lead : LeadList) {
            sb.append(lead).append(", ");
        }
        if (LeadList.size() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        LeadName leadName = new LeadName();
        System.out.println(leadName);
    }
}
