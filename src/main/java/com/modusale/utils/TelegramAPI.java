package com.modusale.utils;

import com.modusale.utils.properties.TelegramProperty;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramAPI {
    private final String sendURL;
    private final String deleteURL;
    private final ModusaleRequest modusaleRequest;

    public TelegramAPI(TelegramProperty telegramProperty, ModusaleRequest modusaleRequest){
        this.sendURL=telegramProperty.getSendURL();
        this.deleteURL=telegramProperty.getDeleteURL();
        this.modusaleRequest = modusaleRequest;
    }

    public void send(String str){
        String encodeStr = URLEncoder.encode(str, StandardCharsets.UTF_8);
        modusaleRequest.asyncSend(this.sendURL + encodeStr,String.class);
        modusaleRequest.asyncSend(this.deleteURL,String.class);
    }
}
