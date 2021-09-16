package com.modusaleJava.server.service;

import com.modusaleJava.server.utils.KafkaProduce;
import com.modusaleJava.server.utils.ModusaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {
    private KafkaProduce kafkaProduce;
    @Autowired
    public void setKafkaProduce(KafkaProduce kafkaProduce) {
        this.kafkaProduce = kafkaProduce;
    }

    private Map<String, List<String>> data=new HashMap<>();
    @KafkaListener(topics ="modusaleData", groupId = "web-server")
    public void consume(String message){
        ModusaleMapper modusaleMapper=new ModusaleMapper();
        Map<String,String> temp=modusaleMapper.jsonToObj(message, HashMap.class);
        for(String key:temp.keySet()){
            String str=temp.get(key);
            str=str.substring(1,str.length()-1);
            data.put(key,Arrays.asList(str.split(", ")));
        }
    }
    public void refresh(){
        kafkaProduce.sendRefreshReq("refresh");
    }

    public Map<String,List<String>> getData(){
        return data;
    }

}
