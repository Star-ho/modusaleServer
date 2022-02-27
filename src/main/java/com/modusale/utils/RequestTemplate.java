package com.modusale.utils;

import com.modusale.ModusaleAppData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class RequestTemplate {

     public <T extends ModusaleAppData> List<T>  removeDup(List<T> dataList){
         Map<String,String> strMap=new HashMap<>();

         for(int i=0;i<dataList.size();i++){
             if(strMap.containsKey(dataList.get(i).getBrandName())){
                 for(int j=0;j<i;j++){
                     if(dataList.get(j).getBrandName().equals(dataList.get(i).getBrandName())){
                         dataList.remove(j);
                         i--;
                     }
                 }
             }else {
                 strMap.put(dataList.get(i).getBrandName(),dataList.get(i).getPrice());
             }
         }
        return dataList;
     }
     abstract public <T extends ModusaleAppData> List<T> getAppData();
}
