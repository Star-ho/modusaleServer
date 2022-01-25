package com.modusaleJava.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.*;


@Component
@ConfigurationProperties("modusale.github")
public class GitHubData {
    private final AppDataObj appDataObj;
    private final String baseURL;
    private final String itemlistCoupang;
    private final String itemlistCoupangImage;
    private final String itemlistCoupangMonthly;
    private final String itemlistWemef;
    private final String menufile;
    private final String unifiedName;
    private final ModusaleRequestTemplate modusaleRequestTemplate;
    private Map<String,String> header;

    public GitHubData(@Autowired AppDataObj appDataObj, @Value("baseURL") String baseURL, @Value("itemlistCoupang") String itemlistCoupang,
                      @Value("itemlistCoupangImage") String itemlistCoupangImage, @Value("itemlistCoupangMonthly") String itemlistCoupangMonthly,
                      @Value("itemlistWemef") String itemlistWemef, @Value("menufile") String menufile,@Value("unifiedName") String unifiedName,
                      @Autowired ModusaleRequestTemplate modusaleRequestTemplate){
        this.appDataObj=appDataObj;
        this.baseURL=baseURL;
        this.itemlistCoupang=itemlistCoupang;
        this.itemlistCoupangImage=itemlistCoupangImage;
        this.itemlistCoupangMonthly=itemlistCoupangMonthly;
        this.itemlistWemef=itemlistWemef;
        this.menufile=menufile;
        this.unifiedName=unifiedName;
        this.modusaleRequestTemplate=modusaleRequestTemplate;
    }

    public void setHeader( Map<String,String> header ){
        this.header=header;
    }

    public Map<String,List<String>> getCoupangItemMap(){
        return parseToMapForCoupang(this.itemlistCoupang,0);
    }

    public Map<String,List<String>> parseToMapForCoupang(String URL, int keyIndex){
        Map<String,List<String>> parsedMap=new HashMap<>();
        List<List<String>> parsedString=parseToList(URL);
        for(List<String> strList:parsedString){
            ArrayList<String > strArrayList=new ArrayList<>(strList);
            String key=strList.get(keyIndex);
            strArrayList.remove(keyIndex);
            parsedMap.put(key,strArrayList);
        }
        return parsedMap;
    }

    public Map<String,List<String>> getCoupangMonthlyMap(){
        return parseToMap(this.itemlistCoupangMonthly,2);
    }

    public List<String> getCoupangImageList(){
        return getGithubData(this.itemlistCoupangImage);
    }

    public Map<String,List<String>> getWemefMap(){
        return parseToMap(this.itemlistWemef,0);
    }

    public void setGithubData(){
        setUnifiedNameMap();
        setCategoryMap();
    }

    public void setCategoryMap(){
        appDataObj.setMenufileMap(parseToMap(this.menufile,0));
    }

    public void setUnifiedNameMap(){
        Map<String,String> unifiedNameMap=new HashMap<>();
        List<List<String>> nameList=parseToList(this.unifiedName);
        for(List<String> strList:nameList){
            for(int i=1;i<strList.size();i++){
                unifiedNameMap.put(strList.get(i),strList.get(0));
            }
        }
        appDataObj.setUnifiedNameMap(unifiedNameMap);
    }

    private Map<String,List<String>> parseToMap(String URL,int keyIndex){
        Map<String,List<String>> parsedMap=new HashMap<>();
        List<List<String>> parsedString=parseToList(URL);
        for(List<String> strList:parsedString){
            parsedMap.put(strList.get(keyIndex),strList);
        }
        return parsedMap;
    }

    private List<List<String>> parseToList(String URL){
        List<List<String>> parsedList=new ArrayList<>();
        List<String> parseCRLF= getGithubData(URL);
        for (String str : parseCRLF) {
            str = str.trim();
            if (str.length() > 0) {
                parsedList.add(Arrays.asList(str.split("\\|\\|")));
            }
        }
        return parsedList;
    }

    private List<String> getGithubData(String URL){
        String data=modusaleRequestTemplate.getResponseDataClass(this.baseURL+URL,this.header,String.class);
        return Arrays.asList(data.split("\n"));
    }
}
