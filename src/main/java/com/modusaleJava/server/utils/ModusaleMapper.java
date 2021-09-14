package com.modusaleJava.server.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


@Component
public class ModusaleMapper {
    private ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    public <T,E> E jsonToObj(String obj, Class<E> tClass){
        try {
            E retObj=mapper.readValue(obj, tClass);
            return retObj;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
