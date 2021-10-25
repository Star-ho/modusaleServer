package com.modusaleJava.server.utils;

import java.util.*;

public class ImgSourceToHTML {

    public String imgToHTML(LinkedHashMap<String, String> imgMap){
        String html="<html><head></head><body>\n";
        String imgURL="<div style=\"float: left;margin: 10;height: 30;width: 45%;\"></div>";

        for(String key:imgMap.keySet()) {
            if(key=="line"){
                html+="<div style='float: left;margin: 10;width: 100%;' >'";
                html+="<p>------------------------------------------------------------------------------------------------------------</p>";
                html+="</div>";
                continue;
            }
            if(key.contains("eats://")) {
                imgURL += "<p>/" + imgMap.get(key).split("//")[2] + "</p>";
                html+="<div style='float: left;margin: 10;width: 45%;' ><img style='float: left;background-color: blueviolet;' src="
                        + imgMap.get(key) + " width='250' height='100' />" +
                        "<p>"+ "coupang"+ key + "</p>" +
                        "</div>\n";
            }else {
                html+="<div style='float: left;margin: 10;width: 45%;' ><img style='float: left;background-color: blueviolet;' src="
                        + imgMap.get(key) + " width='250' height='100' />" +
                        "<p>" + key + "</p>" +
                        "</div>\n";
            }
        }
        html+=imgURL;
        html+="</body></html>";
        return html;
    }
}
