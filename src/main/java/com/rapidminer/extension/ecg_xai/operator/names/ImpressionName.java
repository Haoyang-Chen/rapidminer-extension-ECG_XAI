package com.rapidminer.extension.ecg_xai.operator.names;

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
        ImpressionList.add("--End--");
        ImpressionList.add("--MoveOn--");
    }

    public String[] getImpressions() {
        return ImpressionList.toArray(String[]::new);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImpressionName [");
        for (String impression : ImpressionList) {
            sb.append(impression).append(", ");
        }
        if (ImpressionList.size() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        ImpressionName impressionName = new ImpressionName();
        System.out.println(impressionName);
    }
}
