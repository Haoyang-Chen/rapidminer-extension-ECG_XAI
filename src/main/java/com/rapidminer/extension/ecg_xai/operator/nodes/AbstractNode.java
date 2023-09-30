package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractNode {
    private String name;
    private String type;

    private Set<AbstractNode> parents=new HashSet<>();
    private AbstractNode YesSon=null;
    private AbstractNode NoSon=null;

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

    public void addParent(AbstractNode node, Boolean yes){
        this.parents.add(node);
        if (yes){
            node.setYesSon(this);
        }else{
            node.setNoSon(this);
        }
    }

    public void setYesSon(AbstractNode node){
        this.YesSon=node;
        node.addParent(this, true);
    }

    public void setNoSon(AbstractNode node){
        this.NoSon=node;
        node.addParent(this, false);
    }
}
