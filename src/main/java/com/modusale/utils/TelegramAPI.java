package com.modusale.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramAPI {
    @Value("${modusale.telegram.sendURL}")
    private String sendURL;
    @Value("${modusale.telegram.deleteURL}")
    private String deleteURL;
    private ModusaleRequestTemplate telegramRequest;

    @Autowired
    public void setTelegramRequest(ModusaleRequestTemplate telegramRequest) {
        this.telegramRequest = telegramRequest;
    }

    public void send(String str){
        String encodeStr = URLEncoder.encode(str, StandardCharsets.UTF_8);
        telegramRequest.getResponseAsync(this.sendURL + encodeStr,String.class);
        telegramRequest.getResponseAsync(this.deleteURL,String.class);
    }
}
