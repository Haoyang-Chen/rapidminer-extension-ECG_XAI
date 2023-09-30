package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;
import com.rapidminer.extension.ecg_xai.operator.nodes.StartNode;

import java.util.ArrayList;
import java.util.List;

public class Step {
    public static List<AbstractNode> nodes=new ArrayList<>();
    public Step(){
        StartNode firstNode=new StartNode();
        this.addNode(firstNode);
    }

    public void addNode(AbstractNode node){
        nodes.add(node);
    }
}
