package com.modusaleJava.server.service;

import com.modusaleJava.server.utils.ImgSourceToHTML;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;

@Service
public class ImageService {
    private String imgStr="";

    public void consume(LinkedHashMap<String,String> message){
        ImgSourceToHTML imgSourceToHTML=new ImgSourceToHTML();
        this.imgStr=imgSourceToHTML.imgToHTML(message);
    }

    public String showimg(){
        return imgStr;
    }
}
