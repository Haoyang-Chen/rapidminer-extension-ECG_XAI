package com.rapidminer.extension.ecg_xai.operator.nodes;

public class ImpressionNode extends AbstractNode{
    private String impression;

    public ImpressionNode(){
        setType("Impression");
    }

    public void setImpression(String impression) {
        this.impression=impression;
        this.setName(impression);
    }
}
