package com.modusaleJava.server.web.service;

import com.modusaleJava.server.coupang.CoupangRequest;
import com.modusaleJava.server.utils.ImgSourceToHTML;
import com.modusaleJava.server.wemefo.WemefoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ImageService {
    @Autowired
    private CoupangRequest coupangRequest;
    @Autowired
    private WemefoRequest wemefoRequest;


    public String showimg(){
        LinkedHashMap<String,String> message= coupangRequest.getCoupangBannerList();
        message.putAll(wemefoRequest.getWemefBannerList());
        ImgSourceToHTML imgSourceToHTML=new ImgSourceToHTML();
        return imgSourceToHTML.imgToHTML(message);
    }
}
