package com.modusaleJava.server.coupang;

import com.modusaleJava.server.ModusaleAppData;
import com.modusaleJava.server.coupang.dto.*;
import com.modusaleJava.server.utils.*;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
@ConfigurationProperties("modusale.coupang")
public class CoupangRequest extends RequestTemplate {

    private String URL;
    private Map<String,String> header;

    public void setURL(String URL) {
        this.URL = URL;
    }
    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    private ModusaleMapper modusaleMapper;
    private ModusaleRequestTemplate coupangRequest;
    private GitHubData gitHubData;
    private TelegramAPI telegramAPI;


    @Autowired
    public void setTelegramAPI(TelegramAPI telegramAPI) {
        this.telegramAPI = telegramAPI;
    }

    @Autowired
    public void getGitHubData(GitHubData gitHubData) {
        this.gitHubData=gitHubData;
    }

    @Autowired
    public void setModusaleMapper(ModusaleMapper modusaleMapper){
        this.modusaleMapper=modusaleMapper;
    }

    @Autowired
    public void setCoupangRequest(ModusaleRequestTemplate coupangRequest) {
        this.coupangRequest = coupangRequest;
    }

    @Override
    public List<ModusaleAppData> getAppData(){
        final String alertMsg="*********************\n";
        List<ModusaleAppData> coupangAppDataList=new ArrayList<>();
        List<CoupangJSON_6> coupangBannerList= getBanner();
        Map<String,List<String>> itemFromGithub=gitHubData.getCoupangItemMap();
        Map<String,List<String>> monthlyItemFromGithub= gitHubData.getCouapngMonthlyMap();
        for(CoupangJSON_6 banner:coupangBannerList){
            String monthBannerScheme=banner.getScheme();
            List<CoupangImageJSON_2> monthlyItmes=getMonthlyMenu(banner,monthBannerScheme);
            if(monthlyItmes==null){
                if (itemFromGithub.get(banner.getId()) != null) {
                    if(itemFromGithub.get(banner.getId()).get(0).equals("no")==false) {
                        for(int i=0;i<itemFromGithub.get(banner.getId()).size();i+=2) {
                            CoupangAppData coupangAppData = new CoupangAppData();
                            coupangAppData.setBrandName(itemFromGithub.get(banner.getId()).get(i));
                            coupangAppData.setPrice(itemFromGithub.get(banner.getId()).get(i+1));
                            coupangAppData.setBrandScheme("coupang" + banner.getScheme());
                            coupangAppData.setId(banner.getId());
                            coupangAppData.setImagePath(banner.getImagePath());
                            coupangAppDataList.add(coupangAppData);
                        }
                    }
                } else {
                    telegramAPI.send(alertMsg + "insert no refer!\n" + banner.getId() + "\n" + banner.getImagePath() + "\n" + banner.getScheme() + "\n");
                }
            }else{
                for(CoupangImageJSON_2 monthlyItem : monthlyItmes){
                    if(monthlyItem.getScheme()!=null){
                        monthlyItem.setScheme("coupang"+monthlyItem.getScheme());
                        if(monthlyItemFromGithub.get(monthlyItem.getScheme())!=null) {
                            CoupangAppData coupangAppData = new CoupangAppData();
                            coupangAppData.setBrandName(monthlyItemFromGithub.get(monthlyItem.getScheme()).get(0));
                            coupangAppData.setPrice(monthlyItemFromGithub.get(monthlyItem.getScheme()).get(1));
                            coupangAppData.setBrandScheme("coupang"+banner.getScheme());
                            coupangAppData.setId(monthlyItemFromGithub.get(monthlyItem.getScheme()).get(1));
                            coupangAppData.setImagePath(monthlyItem.getImageUrl());
                            coupangAppDataList.add(coupangAppData);
                        }else {
                            telegramAPI.send(alertMsg+"insert monthly no refer!\n" +"https://img1a.coupangcdn.com"+ monthlyItem.getImageUrl() + "\n" + monthlyItem.getImageUrl());
                        }
                    }
                }
            }
        }
        coupangAppDataList.sort(new Comparator<>() {
            @Override
            public int compare(ModusaleAppData o1, ModusaleAppData o2) {
                return o1.getBrandName().compareTo(o2.getBrandName());
            }
        });

        return removeDup(coupangAppDataList);
    }

    public List<CoupangJSON_6> getBanner(){
        CoupangJSON_1 coupangJson= coupangRequest.getResponseDataClass(this.URL,this.header,CoupangJSON_1.class);
        return coupangJson.getData().getEntityList().get(0).getEntity().getData().getList();
    }


    public LinkedHashMap<String,String> getCoupangBannerList(){
        LinkedHashMap<String, String> imageBannerMap=new LinkedHashMap<>();
        List<CoupangJSON_6> coupangBannerList= getBanner();
        for(CoupangJSON_6 banner:coupangBannerList){
            String monthBannerScheme=banner.getScheme();
            List<CoupangImageJSON_2> monthlyMenu=getMonthlyMenu(banner,monthBannerScheme);
            if(monthlyMenu==null){
                imageBannerMap.put(banner.getId(),banner.getImagePath());
            }else{
                for(CoupangImageJSON_2 monthlyItem:monthlyMenu){
                    if (monthlyItem.getScheme()!=null) {
                        imageBannerMap.put(monthlyItem.getScheme(), "https://img1a.coupangcdn.com/"+ monthlyItem.getImageUrl());
                    }
                }
            }
        }

        List<ModusaleAppData> coupangDataList=getAppData();
        for(ModusaleAppData data:coupangDataList){
            imageBannerMap.put(data.getId(),data.getImagePath());
        }
        return imageBannerMap;
    }


    private List<CoupangImageJSON_2> getMonthlyMenu(CoupangJSON_6 banner,String mounthBannerScheme){
        //월간 할인 메뉴 확인 위한 문자열
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM", Locale.ENGLISH);
        String mouthString=simpleDateFormat.format(new Date()).toUpperCase(Locale.ROOT);
        String lastMonthString=simpleDateFormat.format(new Date().getTime()-1000*60*60*24*10).toUpperCase(Locale.ROOT);

        if(mounthBannerScheme.length()>0){
            String imgURL=URLDecoder.decode(banner.getScheme().split("=")[1],StandardCharsets.US_ASCII);
            String keyParam="";

            String[] temp=imgURL.split("=");
            if(temp.length>1)keyParam=temp[1];

            if(keyParam.startsWith(mouthString)||keyParam.startsWith(lastMonthString)){//월간 할인 확인 분기
                String monthlyHTML=coupangRequest.getResponseDataClass(imgURL,String.class);
                String monthlyImageJSON=Jsoup.parse(monthlyHTML).select("#landing_page").attr("data-landingpage");
                List<CoupangImageJSON_2> coupangImageList=modusaleMapper.jsonToObj(monthlyImageJSON, CoupangImageJSON_1.class).getImages();
                List<String> imageListFromGithub = gitHubData.getCouapngImageList();

                loop: for(CoupangImageJSON_2 couapngImage:coupangImageList){
                    if(couapngImage.getScheme()!=null) {
                        for(String imgFromGithub:imageListFromGithub){
                            if(imgFromGithub.equals(couapngImage.getImageUrl())){
                                continue loop;
                            }
                        }
                        telegramAPI.send("https://t5a.coupangcdn.com/thumbnails/remote/1024x1024"+couapngImage.getImageUrl());
                    }
                }
                return coupangImageList;
            }
        }
        return null;
    }
}
