package com.modusaleJava.server.web;

import com.modusaleJava.server.ModusaleBatch;
import com.modusaleJava.server.service.ImageService;
import com.modusaleJava.server.service.MainService;
import com.modusaleJava.server.service.TodayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class MainController {

    private MainService mainService;
    private ModusaleBatch modusaleBatch;
    private ImageService imageService;
    private TodayService todayService;

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }
    @Autowired
    public void setModusaleBatch(ModusaleBatch modusaleBatch) {
        this.modusaleBatch = modusaleBatch;
    }
    @Autowired
    public void setMainService(MainService mainService) {
        this.mainService = mainService;
    }
    @Autowired
    public void setTodayService(TodayService todayService){this.todayService=todayService;}

    @GetMapping
    public Map<String, List<String>> returnData(){
        return mainService.getData();
    }

    @GetMapping("/refresh")
    public String refreshData(){
        modusaleBatch.consume();
        return "refresh!";
    }

    @GetMapping("/showimg")
    public String showimg(){
        return imageService.showimg();
    }

    @GetMapping("/todaySale")
    public String todaySale(){return todayService.showTodaySale(); }

}

