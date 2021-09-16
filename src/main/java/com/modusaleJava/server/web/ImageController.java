package com.modusaleJava.server.web;

import com.modusaleJava.server.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/showimg")
public class ImageController {
    private ImageService imageService;
    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public String showimg(){
        return imageService.showimg();
    }

}
