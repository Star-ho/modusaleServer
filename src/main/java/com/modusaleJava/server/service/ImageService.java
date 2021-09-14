package com.modusaleJava.server.service;

import com.modusaleJava.server.utils.ImgSourceToHTML;
import com.modusaleJava.server.utils.ModusaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageService {
    private String imgStr="";

    @KafkaListener(topics ="imgData", groupId = "web-server")
    public void consume(String message) throws IOException {
        ImgSourceToHTML imgSourceToHTML=new ImgSourceToHTML();
        this.imgStr=imgSourceToHTML.imgToHTML(message);
    }

    public String showimg(){
        return imgStr;
    }
}
