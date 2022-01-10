package com.modusaleJava.server.web.service;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TodayService {
    private String todaySaleStr="";

    public void consume(Map<String, List<String>> map){
        todaySaleStr="";
        List<Map.Entry<String, List<String>>> entries = new LinkedList<>(map.entrySet());
        entries.sort((o1, o2) ->
            Integer.parseInt(o2.getValue().get(4))-Integer.parseInt(o1.getValue().get(4))
        );
        entries.forEach(entry->{
            todaySaleStr+=entry.getValue().get(0)+" "+entry.getValue().get(4);
            switch (entry.getValue().get(1)){
                case "yogiyo":todaySaleStr+=" - 요기요";break;
                case "coupang":todaySaleStr+=" - 쿠팡이츠";break;
                case "baemin":todaySaleStr+=" - 배달의민족";break;
                case "wemef":todaySaleStr+=" - 위메프오";break;
            }
            todaySaleStr+="<br>";
        });
    }

    public String showTodaySale(){
        return todaySaleStr;
    }
}
