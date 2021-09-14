package com.modusaleJava.server.utils;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ImgSourceToHTML {

    public String imgToHTML(String imgStr){
        ModusaleMapper modusaleMapper=new ModusaleMapper();
        String html="<html><head></head><body>\n";
        Map<String, String> imgMap=modusaleMapper.jsonToObj(imgStr, LinkedHashMap.class);
        List<String> keyList=new ArrayList<>(imgMap.keySet());
        Collections.sort(keyList);
        for(int i=0;i<keyList.size();i++) {
            html+="<div style='float: left;margin: 10;width: 45%;' ><img style='float: left;background-color: blueviolet;' src="
                    + imgMap.get(keyList.get(i)) + " width='250' height='100' />" +
                    "<p>" + keyList.get(i) + "</p>" +
                    "</div>\n";
            if(i%5==0){
                html+="<div style='float: left;margin: 10;width: 100%;' >'";
                html+="<p>------------------------------------------------------------------------------------------------------------</p>";
                html+="</div>";
            }
        }
        html+="</body></html>";
        return html;
    }
}
