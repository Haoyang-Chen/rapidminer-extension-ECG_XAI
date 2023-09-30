package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;

public abstract class AbstractNode {
    private String name;
    private String type;

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getType(){
        return this.type;
    }

}
