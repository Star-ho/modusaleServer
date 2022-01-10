package com.modusaleJava.server.utils;

import com.modusaleJava.server.ModusaleAppData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AppDataObj {
    @Getter@Setter
    private Map<String, List<String>> menufileMap;
    @Getter@Setter
    private Map<String,String> unifiedNameMap;
    @Getter@Setter
    List<ModusaleAppData> coupangDataList= new ArrayList<>();
    @Getter@Setter
    List<ModusaleAppData> yogiyoDataList =new ArrayList<>();
    @Getter@Setter
    List<ModusaleAppData> baeminDataList =new ArrayList<>();
    @Getter@Setter
    List<ModusaleAppData> wemefDataList =new ArrayList<>();


    @Autowired
    private AppDataObj appDataObj;

    public Map<String, List<String>> getDataMapFromBaemin(){
        return getDataMapFrom(baeminDataList,0);
    }

    public Map<String, List<String>> getAllDataMap(){
        Map<String, List<String>> data=new HashMap<>();
        data.putAll(appDataObj.getDataMapFromBaemin());
        data.putAll(appDataObj.getDataMapFromCoupang());
        data.putAll(appDataObj.getDataMapFromWemef());
        data.putAll(appDataObj.getDataMapFromYogiyo());
        return data;
    }

    public Map<String, List<String>> getDataMapFromCoupang(){
        return getDataMapFrom(coupangDataList,100);
    }

    public Map<String, List<String>> getDataMapFromWemef(){
        return getDataMapFrom(wemefDataList,200);
    }

    public Map<String, List<String>> getDataMapFromYogiyo(){
        return getDataMapFrom(yogiyoDataList,300);
    }

    public  <T extends ModusaleAppData> Map<String, List<String>> getDataMapFrom(List<T> modusaleDataList,int startNum){
        Map<String,String > unifiedName =appDataObj.getUnifiedNameMap();
        Map<String,List<String>> cateMappingName=appDataObj.getMenufileMap();
        Map<String,List<String>> retList=new HashMap<>();
        for(int i = 0; i<modusaleDataList.size(); i++){
            ModusaleAppData data= modusaleDataList.get(i);
            if(unifiedName.get(data.getBrandName())!=null){
                data.setBrandName(unifiedName.get(data.getBrandName()));
            }
            if(cateMappingName.get(data.getBrandName())!=null) {
                List<String> cate = cateMappingName.get(data.getBrandName());
                retList.put("" + (startNum+i), Arrays.asList(data.getBrandName(), data.getAppName(), cate.get(1), cate.get(2), data.getPrice(), data.getBrandScheme()));
            }else {
                retList.put("" + (startNum+i), Arrays.asList(data.getBrandName(), data.getAppName(), "undefiend.png", "없음", data.getPrice(), data.getBrandScheme()));
            }
        }
        return retList;
    }
}
