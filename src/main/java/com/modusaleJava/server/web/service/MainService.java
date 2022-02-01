package com.modusaleJava.server.web.service;

import com.modusaleJava.server.coupang.CoupangRequest;
import com.modusaleJava.server.utils.AppDataObj;
import com.modusaleJava.server.utils.GpsData;
import com.modusaleJava.server.utils.TelegramAPI;
import com.modusaleJava.server.yogiyo.YogiyoRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class MainService {

    private final AppDataObj appDataObj;
    private final YogiyoRequest yogiyoRequest;
    private final CoupangRequest coupangRequest;


    public MainService(AppDataObj appDataObj, YogiyoRequest yogiyoRequest, CoupangRequest coupangRequest){
        this.appDataObj=appDataObj;
        this.yogiyoRequest=yogiyoRequest;
        this.coupangRequest=coupangRequest;
    }

    public Map<String,List<String>> getData(){
        return appDataObj.getAllDataMap();
    }

    public Map<String,List<String>> getDataFrom(GpsData gpsData){
        Map<String, List<String>> data=new HashMap<>();
        data.putAll(appDataObj.getDataMapFromBaemin());
        data.putAll(appDataObj.getDataMapFromWemef());
//        GpsData gpsData=new GpsData("37.551555","126.9893033");
        //reactive로 구현
        try {
            data.putAll(appDataObj.getDataMapFrom(coupangRequest.getAppDataByGps(gpsData),100));
        }catch (Exception e){
            telegramAPI.send("coupang error!!!\n"+e);
            System.out.println(e);
        }
        try {
            data.putAll(appDataObj.getDataMapFrom(yogiyoRequest.getAppDataByGps(gpsData),300));
        }catch (Exception e){
            telegramAPI.send("yogiyo error!!!\n"+e);
            System.out.println(e);
        }
        return data;
    }

}
