package com.modusaleJava.server.utils;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import java.util.Map;

@Data
@Component
public class ModusaleRequestTemplate {

    public ResponseSpec getResponseData(String URL,Map<String,String> headers){
        ResponseSpec responseData=null;
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(URL);
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)).build();

        responseData= WebClient.builder()
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
        return responseData;
    }

    public <T> Flux<T> getResponseDataFlux(String URL,Class<T> tClass){
        return  getResponseData(URL,null).bodyToFlux(tClass);
    }
    public <T> Flux<T> getResponseDataFlux(String URL,Map<String,String> headers,Class<T> tClass){
        return  getResponseData(URL,headers).bodyToFlux(tClass);
    }

    public <T> T getResponseDataClass(String URL,Class<T> tClass){
        int i=0;
        T resData=null;
        while (i<10) {
            try {
                resData= getResponseData(URL, null).bodyToMono(tClass).block();
                break;
            }catch (Exception e){
                System.out.println(e);
                i++;
            }
        }
        return resData;
    }

    public <T> T getResponseDataClass(String URL,Map<String,String> headers,Class<T> tClass){
        int i=0;
        T resData=null;
        while (i<10) {
            try {
                resData=getResponseData(URL, headers).bodyToMono(tClass).block();
                i=10;
            }catch(Exception e){
                System.out.println(e);
                i++;
            }
        }
        return resData;
    }
}
