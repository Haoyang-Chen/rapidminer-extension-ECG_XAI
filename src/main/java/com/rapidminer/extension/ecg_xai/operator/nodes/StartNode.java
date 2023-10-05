package com.rapidminer.extension.ecg_xai.operator.nodes;

public class StartNode extends AbstractNode{
    public StartNode(){
        setType("Start Node");
        setIndex(0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndex().toString()).append(": ");
        sb.append("[StartNode]");
        sb.append(", [");
        for (AbstractNode yesSon : YesSon) {
            sb.append(yesSon.getIndex()).append(", ");
        }
        if (!YesSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        StartNode startNode = new StartNode();
        System.out.println(startNode);
    }
}