package com.modusaleJava.server.baemin;


import com.modusaleJava.server.ModusaleAppData;
import com.modusaleJava.server.baemin.dto.BaeminAppData;
import com.modusaleJava.server.baemin.dto.BaeminResponseJSON;

import com.modusaleJava.server.utils.ModusaleRequestTemplate;
import com.modusaleJava.server.utils.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


//데이터를 받아오는 클래스
@Component
public class BaeminRequest extends RequestTemplate {
    @Value("${modusale.baemin.baeminSchemeHeader}")
    private String baeminSchemeHeader;
    @Value("${modusale.baemin.URL}")
    private String URL;
    @Value("${modusale.baemin.category}")
    private List<String> category;
    private ModusaleRequestTemplate baeminRequest;

    @Autowired
    public void setBaeminRequest(ModusaleRequestTemplate baeminRequest) {
        this.baeminRequest = baeminRequest;
    }

    @Override
    public List<ModusaleAppData> getAppData(){
        List<ModusaleAppData> baeminAppDataList=new ArrayList<>();//결과값
        int count=0;//page넘기는 용도

        //카테고리별 flag
        List<Boolean> flagArr=new ArrayList<>();
        for(int i =0;i<category.size();i++)flagArr.add(true);
        while (true){
            int flag=0;//종료 flag
            List<BaeminResponseJSON>  resCateList=null;
            int index=0;
            while (index<10) {
                try {
                    //카테고리 별로 URL, page 넣어서 요청
                    Flux<BaeminResponseJSON> fluxList = Flux.just();
                    for (int i = 0; i < category.size(); i++) {
                        if (flagArr.get(i))
                            fluxList = Flux.concat(fluxList, baeminRequest(String.format(this.URL, category.get(i), count)));
                        else fluxList = Flux.concat(fluxList, Flux.just(new BaeminResponseJSON()));
                    }

                    resCateList= fluxList.collectList().block();//요청한 것들 받아오기
                    index=10;
                }catch (Exception e){
                    System.out.println(e);
                    index++;
                }

            }

            //카테고리별로 데이터 확인
            for(int i=0;i<resCateList.size();i++) {
                var resDataList = resCateList.get(i).getData();//하나의 카테고리의 데이터 받음
                if (flagArr.get(i)) {//flag가 true면서 데이터가 있을때
                    for (var resData : resDataList) {
                        if (resData.getMaxDiscountCouponPrice()!=null) {//할인이 있을때 넣기
                            BaeminAppData baeminAppData = new BaeminAppData();//아래 4줄 함수로 만들어서 해야할지 고민
                            baeminAppData.setBrandName(resData.getBrandName());
                            baeminAppData.setPrice(resData.getMaxDiscountCouponPrice());
                            baeminAppData.setBrandScheme(baeminSchemeHeader + resData.getId());
                            baeminAppDataList.add(baeminAppData);
                        }
                    }
                }
                if(resDataList==null||resDataList.size() < 10) {//10개보다 작으면 데이터가 마지막이므로 false
                    flagArr.set(i, false);
                }
            }
            for(Boolean flagVal:flagArr){
                if(flagVal){
                    flag++;
                }
            }
            count++;
            if(flag==0){
                break;
            }
        }
        baeminAppDataList.sort(new Comparator<ModusaleAppData>() {
            @Override
            public int compare(ModusaleAppData o1, ModusaleAppData o2) {
                return o1.getBrandName().compareTo(o2.getBrandName());
            }
        });
        return removeDup(baeminAppDataList);
    }

    //data를 arrayList로 받기 한줄이라 함수로 따로 만들필요 없을 수 있지만 따로 안만들면 코드가 너무 더러워짐
    public Flux<BaeminResponseJSON> baeminRequest(String URL){
        return baeminRequest.getResponseDataFlux(URL,BaeminResponseJSON.class);
    }
}