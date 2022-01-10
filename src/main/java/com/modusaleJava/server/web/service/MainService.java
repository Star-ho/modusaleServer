package com.modusaleJava.server.web.service;

import com.modusaleJava.server.ModusaleAppData;
import com.modusaleJava.server.coupang.CoupangRequest;
import com.modusaleJava.server.utils.AppDataObj;
import com.modusaleJava.server.utils.GpsData;
import com.modusaleJava.server.utils.TelegramAPI;
import com.modusaleJava.server.yogiyo.YogiyoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {

    @Autowired
    private AppDataObj appDataObj;
    @Autowired
    private YogiyoRequest yogiyoRequest;
    @Autowired
    private CoupangRequest coupangRequest;
    @Autowired
    private TelegramAPI telegramAPI;

    public Map<String,List<String>> getData(){
        return appDataObj.getAllDataMap();
    }

    public Map<String,List<String>> getDataFrom(){
        Map<String, List<String>> data=new HashMap<>();
        data.putAll(appDataObj.getDataMapFromBaemin());
        data.putAll(appDataObj.getDataMapFromWemef());
        GpsData gpsData=new GpsData("37.551555","126.9893033");
        //reactive로 구현
        try {
            data.putAll(appDataObj.getDataMapFrom(coupangRequest.getAppDataByGps(gpsData),100));
        }catch (Exception e){
            System.out.println("coupang error!!!\n"+e.getMessage());
        }
        try {
            data.putAll(appDataObj.getDataMapFrom(yogiyoRequest.getAppDataByGps(gpsData),300));
        }catch (Exception e){
            System.out.println("yogiyo error!!!\n"+e.getMessage());
        }
        return data;
    }

}
