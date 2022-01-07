package com.modusaleJava.server.service;

import com.modusaleJava.server.utils.ImgSourceToHTML;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TodayService {
    private String todaySaleStr="";

    public void consume(Map<String, List<String>> map){
        todaySaleStr="";
        List<Map.Entry<String, List<String>>> entries = new LinkedList<>(map.entrySet());
        entries.sort((o1, o2) -> {
            return Integer.parseInt(o2.getValue().get(4))-Integer.parseInt(o1.getValue().get(4));
        });
        entries.forEach(entry->{todaySaleStr+=(entry.getValue().get(0)+" "+entry.getValue().get(4)+"<br>");});
    }

    public String showTodaySale(){
        return todaySaleStr;
    }
}
