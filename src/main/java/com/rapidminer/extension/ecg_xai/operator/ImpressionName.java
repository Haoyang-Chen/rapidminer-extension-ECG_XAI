package com.rapidminer.extension.ecg_xai.operator;

import java.util.HashSet;
import java.util.Set;

public class ImpressionName {
    public static Set<String> ImpressionList=new HashSet<>();
    static {
        ImpressionList.add("SR");
        ImpressionList.add("SARRH");
        ImpressionList.add("SBRAD");
        ImpressionList.add("STACH");
        ImpressionList.add("AFIB");
        ImpressionList.add("AFLT");
        ImpressionList.add("AVB");
        ImpressionList.add("LBBB");
        ImpressionList.add("RBBB");
        ImpressionList.add("WPW");
        ImpressionList.add("IVCD");
        ImpressionList.add("IMI");
        ImpressionList.add("AMI");
        ImpressionList.add("LMI");
        ImpressionList.add("LVH");
        ImpressionList.add("RVH");
        ImpressionList.add("LAE");
        ImpressionList.add("RAE");
        ImpressionList.add("LAFB");
        ImpressionList.add("LPFB");
    }
}
