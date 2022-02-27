package com.modusale.web.service;

import com.modusale.coupang.CoupangRequest;
import com.modusale.utils.GpsData;
import com.modusale.utils.TelegramAPI;
import com.modusale.yogiyo.YogiyoRequest;
import com.modusale.utils.AppDataObj;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class MainService {

    private final AppDataObj appDataObj;
    private final YogiyoRequest yogiyoRequest;
    private final CoupangRequest coupangRequest;
    private final TelegramAPI telegramAPI;

    public MainService(AppDataObj appDataObj, YogiyoRequest yogiyoRequest, CoupangRequest coupangRequest, TelegramAPI telegramAPI){
        this.appDataObj=appDataObj;
        this.yogiyoRequest=yogiyoRequest;
        this.coupangRequest=coupangRequest;
        this.telegramAPI=telegramAPI;
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
            data.putAll(appDataObj.getDataMapFrom(coupangRequest.getAppDataBy(gpsData),100));
        }catch (Exception e){
            telegramAPI.send("coupang error!!!\n"+e);
            System.out.println("coupang error!!!\n"+e);
        }
        try {
            data.putAll(appDataObj.getDataMapFrom(yogiyoRequest.getAppDataByGps(gpsData),300));
        }catch (Exception e){
            telegramAPI.send("yogiyo error!!!\n"+e);
            System.out.println("yogiyo error!!!\n"+e);
        }
        return data;
    }

}
