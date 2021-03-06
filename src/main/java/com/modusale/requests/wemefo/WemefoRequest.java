package com.modusale.requests.wemefo;

import java.util.*;

import com.modusale.aop.alertMessage.Alert;
import com.modusale.utils.GitHubData;
import com.modusale.utils.ModusaleRequest;
import com.modusale.utils.TelegramAPI;
import com.modusale.utils.properties.WemefoProperty;
import com.modusale.requests.wemefo.dto.WemefAppData;
import com.modusale.requests.wemefo.dto.WemefItem;
import com.modusale.requests.wemefo.dto.WemefMainJSON;
import com.modusale.requests.wemefo.dto.WemefCategory;
import com.modusale.ModusaleAppData;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class WemefoRequest extends ModusaleAppData{
    private final String URL;
    private final Map<String,String> headers;
    private final ModusaleRequest modusaleRequest;
    private final GitHubData gitHubData;
    private final TelegramAPI telegramAPI;

    public WemefoRequest(WemefoProperty wemefoProperty, ModusaleRequest modusaleRequest,
                         GitHubData gitHubData, TelegramAPI telegramAPI){
        this.URL=wemefoProperty.getURL();
        this.headers=wemefoProperty.getHeaders();
        this.modusaleRequest = modusaleRequest;
        this.gitHubData=gitHubData;
        this.telegramAPI=telegramAPI;
    }

    @Alert
    public List<ModusaleAppData> getWemefOData(){
        List<List<String>> itemsFromWeb = getWemefDataFrom();
        Map<String, WemefItem> itemsFromGithub = getGithubWemefItem();
        return mergeData(itemsFromWeb,itemsFromGithub);
    }

    @Alert
    public LinkedHashMap<String,String> getWemefBannerList(){
        LinkedHashMap<String,String> bannerMap=new LinkedHashMap<>();
        List<List<String>> itemsFromWeb =getWemefDataFrom();
        for(List<String> item:itemsFromWeb){
            if(item.size()>2) {
                bannerMap.put(item.get(2),item.get(1));
            }else {
                bannerMap.put(item.get(1),item.get(0));
            }
        }
        return bannerMap;
    }

    @Alert
    private List<List<String>> getWemefDataFrom(){
        String wemefURL = getWemefCouponURL();
        String wemefRes= modusaleRequest.syncDataFrom(wemefURL,String.class);
        return getParsedData(wemefRes);
    }

    private String getWemefCouponURL(){
        WemefMainJSON wemefMainJSON= modusaleRequest.syncDataFrom(this.URL,this.headers, WemefMainJSON.class);
        ArrayList<WemefCategory> wemefCategories=wemefMainJSON.getData().getTemplates().get(1).getItems();
        for(WemefCategory item:wemefCategories){
            if(item.getTitle()!=null &&item.getTitle().contains("????????????")){
                String[] temp=item.getLink().split("=");
                return temp[temp.length-1];
            }
        }
        telegramAPI.send("????????? ??????????????? ????????????");
        throw new NullPointerException();
    }

    private List<List<String>> getParsedData(String wemefRes){
        Elements pTagElement = getPTageElement(wemefRes);
        List<List<String>> wemefItemList = getWemefItemList(pTagElement);
        return removeNullBanner(wemefItemList);
    }

    private Elements getPTageElement(String wemefRes){
        Elements pTag;
        Elements pTagElement;
        try {
            pTag=Jsoup.parse(wemefRes).select("div.view_coupon_desc").select("div div").select("p");//p?????? ??????
            pTagElement=pTag.get(pTag.size()-2).select("p > *");//p?????? ?????????????????? ?????????????????? ??????
        }catch (Exception e){
            pTag=Jsoup.parse(wemefRes).select("div.view_coupon_desc").select("div").select("p");//p?????? ??????
            pTagElement=pTag.get(pTag.size()-1).select("p > *");//p?????? ?????????????????? ?????????????????? ??????
        }

        while(!pTagElement.get(0).toString().contains("data-filename")){//??????????????? ?????????
            pTagElement=pTagElement.next();
        }
        return pTagElement;
    }

    private List<List<String>> getWemefItemList(Elements pTagElement){
        return Arrays.stream(pTagElement.toArray()).map(element->{
            String elementString=element.toString();
            List<String> elementURIList=null;

            if(elementString.contains("a href")){
                String[] splitedElementString=elementString.split("\"");
                splitedElementString[1]=splitedElementString[1].replace("&amp;","&");
                if(elementString.contains("data-filename")){
                    if(splitedElementString[3].startsWith("http")&&splitedElementString.length>5){
                        elementURIList=Arrays.asList(splitedElementString[1],splitedElementString[3],splitedElementString[7]);
                    }else {
                        elementURIList=Arrays.asList(splitedElementString[1],splitedElementString[5],splitedElementString[9]);
                    }
                }else{
                    if(splitedElementString.length<3) {
                        elementURIList = Arrays.asList(splitedElementString[1], splitedElementString[3]);
                    }else{
                        return null;
                    }
                }
            }else{
                String[] splitedElementSting=elementString.split("\"");
                if(splitedElementSting.length>5) {
                    elementURIList=Arrays.asList(splitedElementSting[1],splitedElementSting[7]);
                }
            }
            return elementURIList;
        }).collect(Collectors.toList());
    }

    private List<List<String>> removeNullBanner(List<List<String>> wemefBannerList){
        List<List<String>> bannerListDeletedNull=new ArrayList<>();
        for(List<String> banner:wemefBannerList ){
            if(banner!=null){
                bannerListDeletedNull.add(banner);
            }
        }
        return bannerListDeletedNull;
    }

    private Map<String,WemefItem> getGithubWemefItem(){
        Map<String,WemefItem> wemefItemMap= new HashMap<>();
        Map<String,List<String>> itemMap= gitHubData.getWemefMap();
        for(String item:itemMap.keySet()){
            WemefItem wemefItem=new WemefItem();
            wemefItem.setBrandName(itemMap.get(item).get(0));
            wemefItem.setPrice(itemMap.get(item).get(1));
            wemefItem.setScheme(itemMap.get(item).get(2));
            wemefItemMap.put(itemMap.get(item).get(2),wemefItem);
        }
        return wemefItemMap;
    }

    private List<ModusaleAppData> mergeData(List<List<String>> itemsFromWeb, Map<String,WemefItem> itemsFromGithub){
        List<ModusaleAppData> wemefAppDataList=new ArrayList<>();
        int count=0;
        for(List<String> item:itemsFromWeb){
            if(item.get(0).contains("cupping")){
                String indexScheme=item.size()>2?item.get(2):item.get(1);
                if(itemsFromGithub.get(indexScheme)!=null) {
                    WemefAppData wemefAppData=new WemefAppData();
                    wemefAppData.setBrandName(itemsFromGithub.get(indexScheme).getBrandName());
                    wemefAppData.setPrice(itemsFromGithub.get(indexScheme).getPrice());
                    wemefAppData.setBrandScheme(item.get(0));
                    wemefAppDataList.add(wemefAppData);
                }else{
                    count++;
                }
            }
        }
        if(count>6){
            for(int i=0;i<8;i++)telegramAPI.send("wemefOchange!!");
        }
        return wemefAppDataList;
    }
}