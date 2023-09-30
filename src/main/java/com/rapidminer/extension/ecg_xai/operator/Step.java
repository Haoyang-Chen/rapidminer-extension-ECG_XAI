package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.extension.ecg_xai.operator.nodes.AbstractNode;

import java.util.ArrayList;
import java.util.List;

public class Step {
    public static List<AbstractNode> nodes=new ArrayList<>();

    public void addNode(AbstractNode node){
        nodes.add(node);
    }
}
