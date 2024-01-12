package com.rapidminer.extension.ecg_xai.operator.Structures.IOObjects;

import com.rapidminer.operator.ResultObjectAdapter;

public class StringInfo extends ResultObjectAdapter {
    public String info;
    public String type;
    public StringInfo(String info){
        this.info=info;
    }
    @Override
    public String toString() {
        return info;
    }
}
