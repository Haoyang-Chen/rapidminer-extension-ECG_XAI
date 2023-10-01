package com.rapidminer.extension.ecg_xai.operator.nodes;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode {
    private String type;
    private Integer index;

    public Set<AbstractNode> parents=new HashSet<>();
    public Set<AbstractNode> YesSon=new HashSet<>();
    public Set<AbstractNode> NoSon=new HashSet<>();

    public void setIndex(Integer index){
        this.index=index;
    }

    public Integer getIndex(){
        return this.index;
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
            node.addYesSon(this);
        }else{
            node.addNoSon(this);
        }
    }

    public void addYesSon(AbstractNode node){
        this.YesSon.add(node);
    }

    public void addNoSon(AbstractNode node){
        this.NoSon.add(node);
    }

    public static void main(String[] args) {
        AbstractNode node = new StartNode();
        System.out.println(node);
    }
}
