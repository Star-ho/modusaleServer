package com.modusale.utils;

import com.modusale.aop.retry.Retry;
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
public class ModusaleRequest {

    @Retry
    public <T> void asyncSend(String URL, Class<T> tClass){
        getModusaleWebClient(URL, null).bodyToMono(tClass).subscribe();
    }

    public <T> T syncDataFrom(String URL, Class<T> tClass){
        return syncDataFrom(URL,null,tClass);
    }

    @Retry
    public <T> T syncDataFrom(String URL, Map<String,String> headers, Class<T> tClass){
        return getModusaleWebClient(URL, headers).bodyToMono(tClass).block();
    }

    public <T> List<T> syncSend(List<String> urlList, Class<T> tClass){
        return syncDataListFrom(urlList,null,tClass);
    }

    @Retry
    public <T> List<T> syncDataListFrom(List<String> urlList, Map<String,String> header, Class<T> tClass){
        int i=0;
        Flux<T> webClientList = Flux.just();
        for(String url:urlList){
            //concat이 순서 보장
            webClientList = Flux.concat(webClientList,getModusaleWebClient(url,header).bodyToFlux(tClass));
        }
        return webClientList.collectList().block();
    }

    @Retry
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
                    h.add("Cache-Control","no-cache, no-store, max-age=0, must-revalidate");
                    if (headers != null) {
                        for (String a : headers.keySet()) {
                            h.add(a, headers.get(a));
                        }
                    }
                })
                .retrieve();
    }
}
