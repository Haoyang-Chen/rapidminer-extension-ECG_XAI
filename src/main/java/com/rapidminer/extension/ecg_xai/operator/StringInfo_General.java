package com.rapidminer.extension.ecg_xai.operator;

import com.rapidminer.operator.ResultObjectAdapter;

public class StringInfo_General extends ResultObjectAdapter {
    public String info;
    public StringInfo_General(String info){
        this.info=info;
    }
    @Override
    public String toString() {
        return info;
    }
}
