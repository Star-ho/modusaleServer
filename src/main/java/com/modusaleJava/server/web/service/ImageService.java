package com.modusaleJava.server.web.service;

import com.modusaleJava.server.coupang.CoupangRequest;
import com.modusaleJava.server.utils.ImgSourceToHTML;
import com.modusaleJava.server.wemefo.WemefoRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ImageService {
    private final CoupangRequest coupangRequest;
    private final WemefoRequest wemefoRequest;

    public ImageService(CoupangRequest coupangRequest, WemefoRequest wemefoRequest){
        this.coupangRequest=coupangRequest;
        this.wemefoRequest=wemefoRequest;
    }


    public String showimg(){
        LinkedHashMap<String,String> message= coupangRequest.getCoupangBannerList();
        message.putAll(wemefoRequest.getWemefBannerList());
        ImgSourceToHTML imgSourceToHTML=new ImgSourceToHTML();
        return imgSourceToHTML.imgToHTML(message);
    }
}
