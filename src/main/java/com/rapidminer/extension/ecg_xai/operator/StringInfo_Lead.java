package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.ResultObjectAdapter;

public class StringInfo_Lead extends ResultObjectAdapter {
    public String info;
    public StringInfo_Lead(String info){
        this.info=info;
    }
    @Override
    public String toString() {
        return info;
    }
}
