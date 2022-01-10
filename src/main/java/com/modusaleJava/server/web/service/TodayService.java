package com.modusaleJava.server.web.service;

import com.modusaleJava.server.utils.AppDataObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TodayService {

    @Autowired
    private AppDataObj appDataObj;

    public String showTodaySale(){
        StringBuilder todaySaleStr= new StringBuilder();
        Map<String, List<String>> map=appDataObj.getAllDataMap();

        List<Map.Entry<String, List<String>>> entries = new LinkedList<>(map.entrySet());
        entries.sort((o1, o2) ->
                Integer.parseInt(o2.getValue().get(4))-Integer.parseInt(o1.getValue().get(4))
        );
        for (var entry : entries) {
            todaySaleStr.append(entry.getValue().get(0)).append(" ").append(entry.getValue().get(4));
            switch (entry.getValue().get(1)) {
                case "yogiyo":
                    todaySaleStr.append(" - 요기요");
                    break;
                case "coupang":
                    todaySaleStr.append(" - 쿠팡이츠");
                    break;
                case "baemin":
                    todaySaleStr.append(" - 배달의민족");
                    break;
                case "wemef":
                    todaySaleStr.append(" - 위메프오");
                    break;
            }
            todaySaleStr.append("<br>");
        }
        return todaySaleStr.toString();
    }
}
