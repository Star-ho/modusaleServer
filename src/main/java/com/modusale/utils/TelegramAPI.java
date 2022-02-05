package com.modusale.utils;

import com.modusale.utils.property.TelegramProperty;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramAPI {
    private final String sendURL;
    private final String deleteURL;
    private final ModusaleRequestTemplate modusaleRequestTemplate;

    public TelegramAPI(TelegramProperty telegramProperty, ModusaleRequestTemplate modusaleRequestTemplate){
        this.sendURL=telegramProperty.getSendURL();
        this.deleteURL=telegramProperty.getDeleteURL();
        this.modusaleRequestTemplate=modusaleRequestTemplate;
    }

    public void send(String str){
        String encodeStr = URLEncoder.encode(str, StandardCharsets.UTF_8);
        modusaleRequestTemplate.getResponseAsync(this.sendURL + encodeStr,String.class);
        modusaleRequestTemplate.getResponseAsync(this.deleteURL,String.class);
    }
}
