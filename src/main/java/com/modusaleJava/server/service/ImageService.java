package com.modusaleJava.server.service;

import com.modusaleJava.server.utils.ImgSourceToHTML;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageService {
    private String imgStr="";

    @KafkaListener(topics ="imgData", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) throws IOException {
        ImgSourceToHTML imgSourceToHTML=new ImgSourceToHTML();
        this.imgStr=imgSourceToHTML.imgToHTML(message);
    }

    public String showimg(){
        return imgStr;
    }
}
