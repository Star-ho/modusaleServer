package com.modusale.yogiyo;


import com.modusale.utils.GpsData;
import com.modusale.ModusaleAppData;
import com.modusale.utils.ModusaleRequestTemplate;
import com.modusale.utils.RequestTemplate;
import com.modusale.utils.properties.YogiyoProperty;
import com.modusale.yogiyo.dto.YogiyoAppData;
import com.modusale.yogiyo.dto.YogiyoResponseJSON;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class YogiyoRequest extends RequestTemplate {
    private final String URL;
    private final Map<String,String> headers;
    private final String location;
    private final ModusaleRequestTemplate modusaleRequestTemplate;

    public YogiyoRequest(YogiyoProperty yogiyoProperty, ModusaleRequestTemplate modusaleRequestTemplate){
        this.URL=yogiyoProperty.getURL();
        this.headers=yogiyoProperty.getHeaders();
        this.location=yogiyoProperty.getLocation();
        this.modusaleRequestTemplate=modusaleRequestTemplate;
    }

    @Override
    public List<ModusaleAppData> getAppData(){
        ArrayList<List<String>> locArr=locToArr(this.location);
        List<String> urlList= getUrlList(locArr);
        List<YogiyoResponseJSON> responseList=sendTo(urlList);
        return removeDup(parseTo(responseList));
    }

    public List<ModusaleAppData> getAppDataByGps(GpsData gpsData){
        String url = String.format(this.URL,gpsData.getLongitude(),gpsData.getLatitude());
        YogiyoResponseJSON responseList=sendTo(url);
        return removeDup(parseJSON(responseList));
    }

    private List<String> getUrlList(ArrayList<List<String>> locArr){
        List<String> urlArr=new ArrayList<>();
        for(var loc:locArr){
            urlArr.add(String.format(this.URL,loc.get(1),loc.get(0)));
        }
        return urlArr;
    }

    private YogiyoResponseJSON sendTo(String url){
        YogiyoResponseJSON response = null;
        for (int i=0;i<10;) {
            try {
                response = modusaleRequestTemplate.getResponseDataClass(url,this.headers,YogiyoResponseJSON.class);
                i=10;
            }catch (Exception e){
                System.out.println(e);
                i++;
            }
        }
        return response;
    }

    private List<YogiyoResponseJSON> sendTo(List<String> urlArr){
        List<YogiyoResponseJSON> responseList = null;
        for (int i=0;i<10;) {
            try {
                Flux<YogiyoResponseJSON> fluxList = Flux.just();
                for (var url : urlArr) {
                    fluxList = Flux.concat(fluxList, yogiyoRequest(url));
                }
                responseList = fluxList.collectList().block();
                i=10;
            }catch (Exception e){
                System.out.println(e);
                i++;
            }
        }
        return responseList;
    }

    private Flux<YogiyoResponseJSON> yogiyoRequest(String URL){
        return modusaleRequestTemplate.getResponseDataFlux(URL,this.headers,YogiyoResponseJSON.class);
    }

    private ArrayList<List<String>> locToArr(String location){
        var tempArr=location.substring(1,location.length()-1).split("],\\[");
        var locArr=new ArrayList<List<String>>();
        for(var temp : tempArr){
            locArr.add(Arrays.asList(temp.split(",")));
        }
        return locArr;
    }

    private List<ModusaleAppData> parseTo(List<YogiyoResponseJSON> responseList){
        List<ModusaleAppData> yogiyoAppDataList=new ArrayList<>();

        for(YogiyoResponseJSON response:responseList){
            yogiyoAppDataList.addAll(parseJSON(response));
        }
        yogiyoAppDataList=yogiyoAppDataList.stream().distinct().collect(Collectors.toList());
        yogiyoAppDataList.sort(Comparator.comparing(ModusaleAppData::getBrandName));
        return yogiyoAppDataList;
    }

    private List<ModusaleAppData> parseJSON(YogiyoResponseJSON response){
        List<ModusaleAppData> yogiyoAppDataList=new ArrayList<>();
        for(var item : response.getHotdeals().getItems()){
            YogiyoAppData yogiyoAppData=new YogiyoAppData();
            yogiyoAppData.setBrandName(item.getName());
            yogiyoAppData.setPrice(item.getAdditional_discount());
            yogiyoAppData.setBrandScheme("yogiyoapp://franchise?fr_id="+item.getFranchise_id());
            yogiyoAppDataList.add(yogiyoAppData);
        }
        return yogiyoAppDataList;
    }
}
