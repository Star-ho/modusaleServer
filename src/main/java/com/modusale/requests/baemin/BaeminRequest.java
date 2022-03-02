package com.modusale.requests.baemin;

import com.modusale.aop.alertMessage.Alert;
import com.modusale.requests.baemin.dto.*;
import com.modusale.utils.ModusaleRequest;
import com.modusale.ModusaleAppData;
import com.modusale.utils.RequestTemplate;
import com.modusale.utils.properties.BaeminProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BaeminRequest extends RequestTemplate {
    private final String baeminSchemeHeader;
    private final String appName;
    private final String URL;
    private final List<String> categories;
    private final ModusaleRequest modusaleRequest;

    public BaeminRequest(BaeminProperty baeminProperty, ModusaleRequest modusaleRequest){
        this.baeminSchemeHeader=baeminProperty.getBaeminSchemeHeader();
        this.appName=baeminProperty.getAppName();
        this.URL=baeminProperty.getURL();
        this.categories =baeminProperty.getCategories();
        this.modusaleRequest = modusaleRequest;
    }

    @Alert
    public List<ModusaleAppData> getAppData(){
        List<ModusaleAppData> baeminAppDataList=new ArrayList<>();
        int page=0;
        Categories categories = new Categories(this.categories.stream().map(it->new Category(it,true)).collect(Collectors.toList()));

        while (!categories.isEmpty()){
            List<BaeminResponseJSON> baeminResJSONList=getBaeminDataFrom(categories,page);
            for(int i = 0 ; i<categories.size();i++){
                List<BaeminResponseData> resDataList = baeminResJSONList.get(i).getData();
                addBaeminData(baeminAppDataList, resDataList);
                if (resDataList.size()<10){
                    categories.remove(i);
                }
            }
            page++;
        }
        return removeDup(baeminAppDataList);
    }

    // 위로 올리기
    private void addBaeminData(List<ModusaleAppData> baeminAppDataList,List<BaeminResponseData> resDataList){
        for (var resData : resDataList) {
            if (resData.getMaxDiscountCouponPrice()!=null) {//할인이 있을때 넣기
                BaeminAppData baeminAppData = new BaeminAppData();
                baeminAppData.setAppName(this.appName);
                baeminAppData.setBrandName(resData.getBrandName());
                baeminAppData.setPrice(resData.getMaxDiscountCouponPrice());
                baeminAppData.setBrandScheme(baeminSchemeHeader + resData.getId());
                baeminAppDataList.add(baeminAppData);
            }
        }
    }

    private List<BaeminResponseJSON> getBaeminDataFrom(Categories categories, int page){
        ArrayList<String> URLList = new ArrayList<>();
        for(Category category : categories){
            URLList.add(String.format(this.URL, category.getCategory(), page));
        }
        return modusaleRequest.asyncSend(URLList,BaeminResponseJSON.class);
    }
}