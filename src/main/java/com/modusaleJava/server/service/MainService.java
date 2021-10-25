package com.modusaleJava.server.service;

import com.modusaleJava.server.utils.ModusaleMapper;
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

}
