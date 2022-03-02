package com.modusale.requests.yogiyo;


import com.modusale.utils.GpsData;
import com.modusale.ModusaleAppData;
import com.modusale.utils.ModusaleRequest;
import com.modusale.utils.RequestTemplate;
import com.modusale.utils.properties.YogiyoProperty;
import com.modusale.requests.yogiyo.dto.YogiyoAppData;
import com.modusale.requests.yogiyo.dto.YogiyoResponseJSON;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class YogiyoRequest extends RequestTemplate {
    private final String URL;
    private final Map<String,String> headers;
    private final String location;
    private final ModusaleRequest modusaleRequest;

    public YogiyoRequest(YogiyoProperty yogiyoProperty, ModusaleRequest modusaleRequest){
        this.URL=yogiyoProperty.getURL();
        this.headers=yogiyoProperty.getHeaders();
        this.location=yogiyoProperty.getLocation();
        this.modusaleRequest = modusaleRequest;
    }

    public List<ModusaleAppData> getAppData(){
        List<String> urlList= getUrlList();
        List<YogiyoResponseJSON> responseList=sendTo(urlList);
        return removeDup(getModusaleDataFrom(responseList));
    }

    public List<ModusaleAppData> getAppDataByGps(GpsData gpsData){
        String url = String.format(this.URL,gpsData.getLongitude(),gpsData.getLatitude());
        YogiyoResponseJSON responseList=sendTo(url);
        return removeDup(getModusaleDataFrom(responseList));
    }

    private List<String> getUrlList(){
        ArrayList<List<String>> locArr=locToArr(this.location);
        List<String> urlArr=new ArrayList<>();
        for(var loc:locArr){
            urlArr.add(String.format(this.URL,loc.get(1),loc.get(0)));
        }
        return urlArr;
    }

    private YogiyoResponseJSON sendTo(String url){
        return  modusaleRequest.syncDataFrom(url,this.headers,YogiyoResponseJSON.class);
    }

    private List<YogiyoResponseJSON> sendTo(List<String> urlList){
        return modusaleRequest.syncDataListFrom(urlList,headers,YogiyoResponseJSON.class);
    }

    private ArrayList<List<String>> locToArr(String location){
        var tempArr=location.substring(1,location.length()-1).split("],\\[");
        var locArr=new ArrayList<List<String>>();
        for(var temp : tempArr){
            locArr.add(Arrays.asList(temp.split(",")));
        }
        return locArr;
    }

    private List<ModusaleAppData> getModusaleDataFrom(List<YogiyoResponseJSON> responseList){
        List<ModusaleAppData> yogiyoAppDataList=new ArrayList<>();
        for(YogiyoResponseJSON response:responseList){
            yogiyoAppDataList.addAll(getModusaleDataFrom(response));
        }
        return yogiyoAppDataList;
    }

    private List<ModusaleAppData> getModusaleDataFrom(YogiyoResponseJSON response){
        List<ModusaleAppData> yogiyoAppDataList=new ArrayList<>();
        for(var item : response.getHotdeals().getItems()){
            YogiyoAppData yogiyoAppData=new YogiyoAppData();
            if (item.getName().startsWith("[Brand]")){ yogiyoAppData.setBrandName(item.getName().substring(7)); }
            else {yogiyoAppData.setBrandName(item.getName());}
            yogiyoAppData.setPrice(item.getAdditional_discount());
            yogiyoAppData.setBrandScheme("yogiyoapp://franchise?fr_id="+item.getFranchise_id());
            yogiyoAppDataList.add(yogiyoAppData);
        }
        return yogiyoAppDataList;
    }
}
