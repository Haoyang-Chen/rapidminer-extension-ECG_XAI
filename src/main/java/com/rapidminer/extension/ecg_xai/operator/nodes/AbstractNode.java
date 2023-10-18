package com.rapidminer.extension.ecg_xai.operator.nodes;

import com.rapidminer.extension.ecg_xai.operator.nodes.condition.AbstractCondition;
import com.rapidminer.extension.ecg_xai.operator.nodes.condition.Compare;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractNode {
    private String type;
    private Integer index;

    public String Yesres;

    public String Nores;

    public Set<AbstractNode> parents=new HashSet<>();
    public Set<AbstractNode> YesSon=new HashSet<>();
    public Set<AbstractNode> NoSon=new HashSet<>();

    public void runCheck(){
        for(AbstractNode parent:parents){
            for(AbstractNode grandParent:parent.parents){
                if (Objects.equals(grandParent.type, "Condition")||Objects.equals(grandParent.type, "Start Node")){
                    if (Objects.equals(grandParent.Yesres, "--MoveOn--") && Objects.equals(grandParent.Nores, "--MoveOn--") && (grandParent.YesSon.isEmpty()||grandParent.NoSon.isEmpty())){
                        if(parent.YesSon.contains(this)) {
                            parent.YesSon.remove(this);
                            grandParent.YesSon.add(this);
                            this.parents.remove(parent);
                            this.parents.add(grandParent);
                        }
                        if(parent.NoSon.contains(this)) {
                            parent.NoSon.remove(this);
                            grandParent.NoSon.add(this);
                            this.parents.remove(parent);
                            this.parents.add(grandParent);
                        }
                    }
                }
            }
        }
    }

    public void setIndex(Integer index){
        this.index=index;
    }

    public Integer getIndex(){
        return this.index;
    }

    public AbstractCondition getCondition(){
        return null;
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
