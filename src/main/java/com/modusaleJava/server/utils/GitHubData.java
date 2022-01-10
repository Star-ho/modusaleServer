package com.modusaleJava.server.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.*;


@Component
@ConfigurationProperties("modusale.github")
public class GitHubData {
    private String baseURL;
    private String itemlistCoupang;
    private String itemlistCoupangImage;
    private String itemlistCoupangMonthly;
    private String itemlistWemef;
    private String menufile;
    private String unifiedName;
    @Getter
    private Map<String,List<String>> menufileMap;
    @Getter
    private Map<String,String> unifiedNameMap;
    private Map<String,String> header;
    private ModusaleRequestTemplate modusaleRequestTemplate;

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
    public void setMenufile(String menufile) {
        this.menufile = menufile;
    }
    public void setUnifiedName(String unifiedName) {
        this.unifiedName = unifiedName;
    }
    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
    public void setItemlistCoupang(String itemlistCoupang) {
        this.itemlistCoupang = itemlistCoupang;
    }
    public void setItemlistCoupangImage(String itemlistCoupangImage) {
        this.itemlistCoupangImage = itemlistCoupangImage;
    }
    public void setItemlistCoupangMonthly(String itemlistCoupangMonthly) {
        this.itemlistCoupangMonthly = itemlistCoupangMonthly;
    }
    public void setItemlistWemef(String itemlistWemef) {
        this.itemlistWemef = itemlistWemef;
    }

    @Autowired
    public void setModusaleRequestTemplate(ModusaleRequestTemplate modusaleRequestTemplate) {
        this.modusaleRequestTemplate = modusaleRequestTemplate;
    }

    public Map<String,List<String>> getCoupangItemMap(){
        return parseToMapforCoupang(this.itemlistCoupang,0);
    }

    public Map<String,List<String>> parseToMapforCoupang(String URL,int keyIndex){
        Map<String,List<String>> parsedMap=new HashMap<>();
        List<List<String>> parsedString=parseToList(URL);
        for(List<String> strList:parsedString){
            ArrayList<String > strArrayList=new ArrayList<>();
            strArrayList.addAll(strList);
            String key=strList.get(keyIndex);
            strArrayList.remove(keyIndex);
            parsedMap.put(key,strArrayList);
        }
        return parsedMap;
    }

    public Map<String,List<String>> getCoupangMonthlyMap(){
        return parseToMap(this.itemlistCoupangMonthly,2);
    }

    public List<String> getCouapngImageList(){
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
        this.menufileMap=parseToMap(this.menufile,0);
    }

    public void setUnifiedNameMap(){
        Map<String,String> unifiedNameMap=new HashMap<>();
        List<List<String>> nameList=parseToList(this.unifiedName);
        for(List<String> strList:nameList){
            for(int i=1;i<strList.size();i++){
                unifiedNameMap.put(strList.get(i),strList.get(0));
            }
        }
        this.unifiedNameMap=unifiedNameMap;
    }

    public Map<String,List<String>> parseToMap(String URL,int keyIndex){
        Map<String,List<String>> parsedMap=new HashMap<>();
        List<List<String>> parsedString=parseToList(URL);
        for(List<String> strList:parsedString){
            parsedMap.put(strList.get(keyIndex),strList);
        }
        return parsedMap;
    }

    public List<List<String>> parseToList(String URL){
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

    public List<String> getGithubData(String URL){
        String data=modusaleRequestTemplate.getResponseDataClass(this.baseURL+URL,this.header,String.class);
        return Arrays.asList(data.split("\n"));
    }
}
