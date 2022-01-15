package com.modusaleJava.server.yogiyo.dto;


import com.modusaleJava.server.ModusaleAppData;

public class YogiyoAppData extends ModusaleAppData {
    public YogiyoAppData(){
        this.setAppName("yogiyo");
    }
    public void setBrandName(String brandName) {
        if (brandName.startsWith("[Brand]")){
            this.brandName = brandName.substring(7);
        }else {
            this.brandName = brandName;
        }
    }
}
