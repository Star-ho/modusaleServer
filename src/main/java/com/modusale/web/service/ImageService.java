package com.modusale.web.service;

import com.modusale.utils.ImgSourceToHTML;
import com.modusale.requests.coupang.CoupangRequest;
import com.modusale.requests.wemefo.WemefoRequest;
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
        LinkedHashMap<String,String> message= coupangRequest.getCoupangBannerImage();
        message.putAll(wemefoRequest.getWemefBannerList());
        ImgSourceToHTML imgSourceToHTML=new ImgSourceToHTML();
        return imgSourceToHTML.imgToHTML(message);
    }
}
