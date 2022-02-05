package com.modusale.coupang;

import com.modusale.coupang.dto.*;
import com.modusale.utils.*;
import com.modusale.utils.property.CoupangProperty;
import com.modusale.ModusaleAppData;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CoupangRequest extends RequestTemplate {

    final String alertMsg="*********************\n";
    private final String URL;
    private final Map<String,String> header;
    private final Map<String,String> gpsHeader;
    private final ModusaleMapper modusaleMapper;
    private final ModusaleRequestTemplate modusaleRequestTemplate;
    private final GitHubData gitHubData;
    private final TelegramAPI telegramAPI;


    public CoupangRequest(TelegramAPI telegramAPI, GitHubData gitHubData, ModusaleMapper modusaleMapper,
                          ModusaleRequestTemplate modusaleRequestTemplate, CoupangProperty coupangProperty){
        this.telegramAPI=telegramAPI;
        this.modusaleMapper=modusaleMapper;
        this.gitHubData=gitHubData;
        this.modusaleRequestTemplate=modusaleRequestTemplate;
        this.URL=coupangProperty.getURL();
        this.header=coupangProperty.getHeader();
        this.gpsHeader=new HashMap<>();
        gpsHeader.putAll(coupangProperty.getHeader());
    }


    @Override
    public List<ModusaleAppData> getAppData(){
        CoupangJSON_1 coupangJson= modusaleRequestTemplate.getResponseDataClass(this.URL,this.header,CoupangJSON_1.class);
        List<CoupangJSON_6> coupangBannerList = coupangJson.getData().getEntityList().get(0).getEntity().getData().getList();
        return removeDup(parseTo(coupangBannerList));
    }

    public List<ModusaleAppData> getAppDataByGps(GpsData gps){
        gpsHeader.put("X-EATS-LOCATION", "{\"addressId\":0,\"latitude\":"+gps.getLatitude()+",\"longitude\":"+gps.getLongitude()+",\"regionId\":10,\"siDo\":\"%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C\",\"siGunGu\":\"%EC%A4%91%EA%B5%AC\"}");
        CoupangJSON_1 coupangJson= modusaleRequestTemplate.getResponseDataClass(this.URL,gpsHeader,CoupangJSON_1.class);
        List<CoupangJSON_6> coupangBannerList = coupangJson.getData().getEntityList().get(0).getEntity().getData().getList();
        return removeDup(parseTo(coupangBannerList));
    }

    public List<CoupangJSON_6> getBanner(){
        CoupangJSON_1 coupangJson= modusaleRequestTemplate.getResponseDataClass(this.URL,this.header,CoupangJSON_1.class);
        return coupangJson.getData().getEntityList().get(0).getEntity().getData().getList();
    }

    private List<ModusaleAppData> parseTo(List<CoupangJSON_6> coupangBannerList){
        Map<String,List<String>> itemFromGithub=gitHubData.getCoupangItemMap();
        Map<String,List<String>> monthlyItemFromGithub= gitHubData.getCoupangMonthlyMap();

        List<ModusaleAppData> coupangAppDataList=new ArrayList<>();

        for(CoupangJSON_6 banner:coupangBannerList){
            String monthBannerScheme=banner.getScheme();
            List<CoupangImageJSON_2> monthlyItems = getMonthlyMenu(banner, monthBannerScheme);

            if(monthlyItems==null){
                if (itemFromGithub.get(banner.getId()) != null) {
                    if(!itemFromGithub.get(banner.getId()).get(0).equals("no")) {
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
                for(CoupangImageJSON_2 monthlyItem : monthlyItems){
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
        coupangAppDataList.sort(Comparator.comparing(ModusaleAppData::getBrandName));

        return coupangAppDataList;
    }

    public LinkedHashMap<String,String> getCoupangBannerList(){
        LinkedHashMap<String, String> imageBannerMap=new LinkedHashMap<>();
        List<CoupangJSON_6> coupangBannerList= getBanner();
        for(CoupangJSON_6 banner:coupangBannerList){
            String monthBannerScheme=banner.getScheme();
            List<CoupangImageJSON_2> monthlyMenu = getMonthlyMenu(banner,monthBannerScheme);

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
            String imgURL;
            try {
                imgURL=URLDecoder.decode(banner.getScheme().split("=")[1],StandardCharsets.US_ASCII);
            }catch (Exception e){
                return null;
            }

            String keyParam="";

            String[] temp=imgURL.split("=");
            if(temp.length>1)keyParam=temp[1];

            if(keyParam.startsWith(mouthString)||keyParam.startsWith(lastMonthString)){//월간 할인 확인 분기
                String monthlyHTML= modusaleRequestTemplate.getResponseDataClass(imgURL,String.class);
                String monthlyImageJSON=Jsoup.parse(monthlyHTML).select("#landing_page").attr("data-landingpage");
                List<CoupangImageJSON_2> coupangImageList=modusaleMapper.jsonToObj(monthlyImageJSON, CoupangImageJSON_1.class).getImages();
                List<String> imageListFromGithub = gitHubData.getCoupangImageList();

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
