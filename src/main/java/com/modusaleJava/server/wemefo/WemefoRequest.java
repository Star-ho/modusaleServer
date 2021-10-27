package com.modusaleJava.server.wemefo;

import java.util.*;

import com.modusaleJava.server.ModusaleAppData;
import com.modusaleJava.server.utils.GitHubData;
import com.modusaleJava.server.utils.ModusaleRequestTemplate;
import com.modusaleJava.server.utils.TelegramAPI;
import com.modusaleJava.server.wemefo.dto.WemefAppData;
import com.modusaleJava.server.wemefo.dto.WemefItem;
import com.modusaleJava.server.wemefo.dto.WemefJSON_1;
import com.modusaleJava.server.wemefo.dto.WemefJSON_4;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties("modusale.wemefo")
public class WemefoRequest{
    private String URL;
    private Map<String,String> headers;

    public void setURL(String URL) {
        this.URL = URL;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    private ModusaleRequestTemplate wemefORequest;
    private GitHubData gitHubData;
    private TelegramAPI telegramAPI;

    @Autowired
    public void getGitHubData(GitHubData gitHubData) {
        this.gitHubData=gitHubData;
    }

    @Autowired
    public void setModusaleRequestTemplate(ModusaleRequestTemplate modusaleRequestTemplate){
        this.wemefORequest = modusaleRequestTemplate;
    }

    @Autowired
    public void setTelegramAPI(TelegramAPI telegramAPI) {
        this.telegramAPI = telegramAPI;
    }

    public List<ModusaleAppData> getWemefOData(){
        List<List<String>> itemsFromWeb =getParsedData();
        Map<String, WemefItem> itemsFromFile = getWemefItemObject();
        return mergeData(itemsFromWeb,itemsFromFile);
    }

    public LinkedHashMap<String,String> getWemefBannerList(){
        LinkedHashMap<String,String> bannerMap=new LinkedHashMap<>();

        List<List<String>> htmlParse =getParsedData();
        for(List<String> data:htmlParse){
            if(data.size()>2) {
                bannerMap.put(data.get(2),data.get(1));
            }else {
                bannerMap.put(data.get(1),data.get(0));
            }
        }
        return bannerMap;
    }

    private List<List<String>> getParsedData(){
        String wemefURL=getWemefCouponURL();
        String wemefRes=wemefORequest.getResponseDataClass(wemefURL,String.class);
        return WemefHTMLparse(wemefRes);
    }

    private List<List<String>> WemefHTMLparse(String wemefRes){
        Elements pTag=Jsoup.parse(wemefRes).select("div.view_coupon_desc").select("div div").select("p");//p태그 파싱
        Elements pTagElement=pTag.get(pTag.size()-2).select("p > *");//p태그 엘리먼트에서 쓸모없는것들 빼기

        while(pTagElement.get(0).toString().contains("data-filename")==false){//쓸모없는거 날리기
            pTagElement=pTagElement.next();
        }

        List<List<String>> wemefBannerList = Arrays.stream(pTagElement.toArray()).map(element->{
            String elementString=element.toString();
            List<String> elementURIList=null;
            if(elementString.contains("a href")){
                String[] splitedElementSting=elementString.split("\"");
                splitedElementSting[1]=splitedElementSting[1].replace("&amp;","&");
                if(elementString.contains("data-filename")){
                    elementURIList=Arrays.asList(splitedElementSting[1],splitedElementSting[3],splitedElementSting[7]);
                }else{
                    elementURIList=Arrays.asList(splitedElementSting[1],splitedElementSting[3]);
                }
            }else{
                String[] splitedElementSting=elementString.split("\"");
                if(splitedElementSting.length>5) {
                    elementURIList=Arrays.asList(splitedElementSting[1],splitedElementSting[7]);
                }

            }
            return elementURIList;
        }).collect(Collectors.toList());

        List<List<String>> remveNullBannerList=new ArrayList<>();
        for(List<String> banner:wemefBannerList ){
            if(banner!=null){
                remveNullBannerList.add(banner);
            }
        }
        return remveNullBannerList;
    }

    private String getWemefCouponURL() throws NullPointerException{
        String wemefURL="";
        WemefJSON_1 resForURL=wemefORequest.getResponseDataClass(this.URL,this.headers,WemefJSON_1.class);
        ArrayList<WemefJSON_4> items=resForURL.getData().getTemplates().get(1).getItems();
        for(WemefJSON_4 item:items){
            if(item.getTitle()!=null &&item.getTitle().contains("쿠폰모음")){
                String[] temp=item.getLink().split("=");
                wemefURL=temp[temp.length-1];
            }
        }
        return wemefURL;
    }

    private Map<String,WemefItem> getWemefItemObject(){
        Map<String,WemefItem> wemefItemMap= new HashMap<>();
        Map<String,List<String>> itemMap= gitHubData.getWemefMap();
        try {
            for(String item:itemMap.keySet()){
                WemefItem wemefItem=new WemefItem();//순서대로 첫번쨰꺼는 브랜드명, 두번째거 가격, 세번째 스킴에 넣음
                wemefItem.setBrandName(itemMap.get(item).get(0));
                wemefItem.setPrice(itemMap.get(item).get(1));
                wemefItem.setScheme(itemMap.get(item).get(2));
                wemefItemMap.put(itemMap.get(item).get(2),wemefItem);
            }
        }catch (Exception e){
            telegramAPI.send(e.getMessage());
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
//                    System.out.println(item);
                    count++;
                }
            }
        }
        if(count>6){
            for(int i=0;i<8;i++)telegramAPI.send("wemefOchange!!");
        }
        wemefAppDataList.sort(new Comparator<ModusaleAppData>() {
            @Override
            public int compare(ModusaleAppData o1, ModusaleAppData o2) {
                return o1.getBrandName().compareTo(o2.getBrandName());
            }
        });
        return wemefAppDataList;
    }
}