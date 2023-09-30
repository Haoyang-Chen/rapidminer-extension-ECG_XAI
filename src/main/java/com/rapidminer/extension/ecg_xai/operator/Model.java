package com.rapidminer.extension.ecg_xai.operator;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public static List<Step> steps=new ArrayList<>();

    public void addStep(Step step){
        steps.add(step);
    }
}
