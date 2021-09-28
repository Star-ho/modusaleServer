package com.modusaleJava.server.utils;

import java.util.*;

public class ImgSourceToHTML {

    public String imgToHTML(String imgStr){
        ModusaleMapper modusaleMapper=new ModusaleMapper();
        String html="<html><head></head><body>\n";
        HashMap<String, String> imgMap=modusaleMapper.jsonToObj(imgStr, HashMap.class);
        List<String> keyList = new ArrayList<>(imgMap.keySet());
        keyList.sort((s1, s2)->s1.compareTo(s2));
        String imgURL="<div style=\"float: left;margin: 10;height: 30;width: 45%;\"></div>";

        for(String key:keyList) {
            if(key=="line"){
                html+="<div style='float: left;margin: 10;width: 100%;' >'";
                html+="<p>------------------------------------------------------------------------------------------------------------</p>";
                html+="</div>";
                continue;
            }
            html+="<div style='float: left;margin: 10;width: 45%;' ><img style='float: left;background-color: blueviolet;' src="
                    + imgMap.get(key) + " width='250' height='100' />" +
                    "<p>" + key + "</p>" +
                    "</div>\n";
            if(key.contains("eats://")){
                imgURL+="<p>/"+imgMap.get(key).split("//")[2]+"</p>";
            }
        }
        html+=imgURL;
        html+="</body></html>";
        return html;
    }
}
