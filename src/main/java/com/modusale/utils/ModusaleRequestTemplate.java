package com.modusale.utils;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Data
@Component
public class ModusaleRequestTemplate {

    public <T> T syncDataFrom(String URL, Class<T> tClass){
        return syncDataFrom(URL,null,tClass);
    }

    public <T> T syncDataFrom(String URL, Map<String,String> headers, Class<T> tClass){
        int i=0;
        T resData=null;
        while (i<10) {
            try {
                resData= getModusaleWebClient(URL, headers).bodyToMono(tClass).block();
                break;
            }catch(Exception e){
                System.out.println(e);
                i++;
            }
        }
        return resData;
    }

    public <T> void asyncDataFrom(String URL, Class<T> tClass){
        int i=0;
        while (i<10) {
            try {
                getModusaleWebClient(URL, null).bodyToMono(tClass).subscribe();
                break;
            }catch (Exception e){
                System.out.println(e);
                i++;
            }
        }
    }

    public <T> List<T> asyncDataFrom(List<String> urlList, Class<T> tClass){
        return asyncDataFrom1(urlList,null,tClass);
    }

    public <T> List<T> asyncDataFrom1(List<String> urlList, Map<String,String> header,Class<T> tClass){
        Flux<T> webClientList = Flux.just();
        for(String url:urlList){
            //concat이 순서 보장
            webClientList = Flux.concat(webClientList,getModusaleWebClient(url,header).bodyToFlux(tClass));
        }
        return webClientList.collectList().block();
    }

    public <T> Flux<T> asyncDataFrom(String URL, Map<String,String> headers, Class<T> tClass){
        return  getModusaleWebClient(URL,headers).bodyToFlux(tClass);
    }


    private ResponseSpec getModusaleWebClient(String URL, Map<String,String> headers){
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(URL);
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)).build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build()
                .get()
                .uri(uriBuilderFactory.builder().build())
                .headers(h -> {
                    if (headers != null) {
                        for (String a : headers.keySet()) {
                            h.add(a, headers.get(a));
                        }
                    }
                })
                .retrieve();
    }
}
