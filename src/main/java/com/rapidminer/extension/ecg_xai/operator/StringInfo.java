package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.ResultObjectAdapter;

public class StringInfo extends ResultObjectAdapter {
    public String info;
    public StringInfo(String info){
        this.info=info;
    }
    @Override
    public String toString() {
        return info;
    }
}
