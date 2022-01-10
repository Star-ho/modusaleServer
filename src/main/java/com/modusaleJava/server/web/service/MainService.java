package com.modusaleJava.server.web.service;

import com.modusaleJava.server.utils.GpsData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {

    private Map<String, List<String>> data=new HashMap<>();

    public void consume(Map<String,List<String>> temp){
        data=temp;
    }

    public Map<String,List<String>> getData(){
        return data;
    }

    public Map<String,List<String>> getDataFrom(GpsData gpsData){
        return data;
    }

}
