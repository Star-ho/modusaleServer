package com.modusale.coupang;

import com.modusale.coupang.dto.*;
import com.modusale.utils.*;
import com.modusale.utils.properties.CoupangProperty;
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
    private Map<String,List<String>> itemFromGithub;
    private List<String> imageListFromGithub;
    private Map<String,List<String>> monthlyItemFromGithub;
    private final String URL;
    private final Map<String,String> header;
    private final ModusaleMapper modusaleMapper;
    private final ModusaleRequest modusaleRequest;
    private final TelegramAPI telegramAPI;
    private final GitHubData gitHubData;

    public CoupangRequest(TelegramAPI telegramAPI, GitHubData gitHubData, ModusaleMapper modusaleMapper,
                          ModusaleRequest modusaleRequest, CoupangProperty coupangProperty){
        this.telegramAPI=telegramAPI;
        this.modusaleMapper=modusaleMapper;
        this.modusaleRequest = modusaleRequest;
        this.URL=coupangProperty.getURL();
        this.header=coupangProperty.getHeader();
        this.itemFromGithub=gitHubData.getCoupangItemMap();
        this.monthlyItemFromGithub= gitHubData.getCoupangMonthlyMap();
        this.imageListFromGithub = gitHubData.getCoupangImageList();
        this.gitHubData=gitHubData;
    }

    public List<ModusaleAppData> getAppDataBy(GpsData gps){
        Map<String, String> gpsHeader = new HashMap<>(this.header);
        gpsHeader.put("X-EATS-LOCATION", "{\"addressId\":0,\"latitude\":"+gps.getLatitude()+",\"longitude\":"+gps.getLongitude()+",\"regionId\":10,\"siDo\":\"%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C\",\"siGunGu\":\"%EC%A4%91%EA%B5%AC\"}");
        CoupangJSON_1 coupangJson= modusaleRequest.syncDataFrom(this.URL,gpsHeader,CoupangJSON_1.class);
        List<ModusaleAppData> parsedDataList = getDataFrom(coupangJson);
        return removeDup(parsedDataList);
    }

    public List<ModusaleAppData> getAppData(){
        CoupangJSON_1 coupangJson= modusaleRequest.syncDataFrom(this.URL,this.header,CoupangJSON_1.class);
        itemFromGithub=gitHubData.getCoupangItemMap();
        monthlyItemFromGithub=gitHubData.getCoupangMonthlyMap();
        imageListFromGithub=gitHubData.getCoupangImageList();
        List<ModusaleAppData> parsedDataList = getDataFrom(coupangJson);
        return removeDup(parsedDataList);
    }

    private List<ModusaleAppData> getDataFrom(CoupangJSON_1 coupangJson){
        List<CoupangJSON_6> coupangBannerList = getBannerList(coupangJson);
        List<ModusaleAppData> coupangAppDataList=new ArrayList<>();

        if (coupangBannerList==null)return coupangAppDataList;
        for(CoupangJSON_6 banner:coupangBannerList){
            String monthBannerScheme=banner.getScheme();
            List<CoupangImageJSON_2> monthlyItems = getMonthlyMenu(banner, monthBannerScheme);
            if(monthlyItems==null){
                ModusaleAppData modusaleAppData=getModusaleDataFrom(banner);
                if (modusaleAppData != null) coupangAppDataList.add(modusaleAppData);
            }else{
                coupangAppDataList.addAll(getModusaleDataFrom(monthlyItems,banner));
            }
        }
        return coupangAppDataList;
    }

    private ModusaleAppData getModusaleDataFrom(CoupangJSON_6 banner){
        if (itemFromGithub.get(banner.getId()) != null) {
            if(!itemFromGithub.get(banner.getId()).get(0).equals("no")) {//할인정보가 아닌 베너 제거
                CoupangAppData coupangAppData = new CoupangAppData();
                coupangAppData.setBrandName(itemFromGithub.get(banner.getId()).get(0));
                coupangAppData.setPrice(itemFromGithub.get(banner.getId()).get(1));
                coupangAppData.setBrandScheme("coupang" + banner.getScheme());
                coupangAppData.setId(banner.getId());
                coupangAppData.setImagePath(banner.getImagePath());
                return coupangAppData;
            }
        } else {
            telegramAPI.send(alertMsg + "쿠팡 배너 이미지 없음!\n" + banner.getId() + "\n" + banner.getImagePath() + "\n" + banner.getScheme() + "\n");
        }
        return null;
    }

    private List<ModusaleAppData> getModusaleDataFrom(List<CoupangImageJSON_2> monthlyItems, CoupangJSON_6 banner) {
        List<ModusaleAppData> monthlyDataList=new ArrayList<>();
        for(CoupangImageJSON_2 monthlyItem : monthlyItems){
            if(monthlyItem.getScheme()!=null){
                monthlyItem.setScheme("coupang"+monthlyItem.getScheme());//결과 셋을 맞추기위해 추가
                if(monthlyItemFromGithub.get(monthlyItem.getScheme())!=null) {
                    CoupangAppData coupangAppData = new CoupangAppData();
                    coupangAppData.setBrandName(monthlyItemFromGithub.get(monthlyItem.getScheme()).get(0));
                    coupangAppData.setPrice(monthlyItemFromGithub.get(monthlyItem.getScheme()).get(1));
                    coupangAppData.setBrandScheme("coupang"+banner.getScheme());
                    coupangAppData.setId(monthlyItemFromGithub.get(monthlyItem.getScheme()).get(1));
                    coupangAppData.setImagePath(monthlyItem.getImageUrl());
                    monthlyDataList.add(coupangAppData);
                }else {
                    telegramAPI.send(alertMsg+"월간 할인 가격 없음!\n" +"https://img1a.coupangcdn.com"+ monthlyItem.getImageUrl() + "\n" + monthlyItem.getImageUrl());
                }
            }
        }
        return monthlyDataList;
    }

    public LinkedHashMap<String,String> getCoupangBannerImage(){
        LinkedHashMap<String, String> imageBannerMap=new LinkedHashMap<>();
        CoupangJSON_1 coupangJson= modusaleRequest.syncDataFrom(this.URL,this.header,CoupangJSON_1.class);
        List<CoupangJSON_6> coupangBannerList= getBannerList(coupangJson);
        if(coupangBannerList==null)return imageBannerMap;
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
        return imageBannerMap;
    }

    private List<CoupangJSON_6> getBannerList(CoupangJSON_1 coupangJson){
        try {
            return coupangJson.getData().getEntityList().get(0).getEntity().getData().getList();
        }catch (Exception e){
            //배너리스트가 없는 경우
            return null;
        }

    }

    private List<CoupangImageJSON_2> getMonthlyMenu(CoupangJSON_6 banner,String monthBannerScheme){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM", Locale.ENGLISH);
        String mouthString=simpleDateFormat.format(new Date()).toUpperCase(Locale.ROOT);
        String lastMonthString=simpleDateFormat.format(new Date().getTime()-1000*60*60*24*10).toUpperCase(Locale.ROOT);

        if(monthBannerScheme.length()>0){
            String imgURL;
            try {
                imgURL=URLDecoder.decode(banner.getScheme().split("=")[1],StandardCharsets.US_ASCII);
            }catch (Exception e){
                //등호가 없는 경우
                //차피 배너가 없기에 null처리 해도 됨
                return null;
            }

            String keyParam="";

            String[] temp=imgURL.split("=");
            if(temp.length>1)keyParam=temp[1];

            if(keyParam.startsWith(mouthString)||keyParam.startsWith(lastMonthString)){//월간 할인 확인 분기
                String monthlyHTML= modusaleRequest.syncDataFrom(imgURL,String.class);
                String monthlyImageJSON=Jsoup.parse(monthlyHTML).select("#landing_page").attr("data-landingpage");
                List<CoupangImageJSON_2> coupangImageList=modusaleMapper.jsonToObj(monthlyImageJSON, CoupangImageJSON_1.class).getImages();

                loop: for(CoupangImageJSON_2 coupangImage:coupangImageList){
                    if(coupangImage.getScheme()!=null) {
                        for(String imgFromGithub:imageListFromGithub){
                            if(imgFromGithub.equals(coupangImage.getImageUrl())){
                                continue loop;
                            }
                        }
                        telegramAPI.send("월간할인 이미지 없음"+"https://t5a.coupangcdn.com/thumbnails/remote/1024x1024"+coupangImage.getImageUrl());
                    }
                }
                return coupangImageList;
            }
        }
        return null;
    }
}
