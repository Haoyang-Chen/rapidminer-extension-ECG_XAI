package com.rapidminer.extension.ecg_xai.operator.nodes;

public class ImpressionNode extends AbstractNode{
    private String impression;

    public ImpressionNode(String impression){
        setType("Impression");
        this.impression=impression;
    }

    public void setImpression(String impression) {
        this.impression=impression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndex().toString()).append(": ");
        sb.append("[ImpressionNode], ").append(impression);
        sb.append(", [");
        for (AbstractNode parent : parents) {
            sb.append(parent.getIndex()).append(", ");
        }
        if (!parents.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append(", [");
        for (AbstractNode yesSon : YesSon) {
            sb.append(yesSon.getIndex()).append(", ");
        }
        if (!YesSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append(", [");
        for (AbstractNode noSon : NoSon) {
            sb.append(noSon.getIndex()).append(", ");
        }
        if (!NoSon.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        ImpressionNode impressionNode = new ImpressionNode("AFIB");
        System.out.println(impressionNode);
    }
}
