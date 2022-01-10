package com.modusaleJava.server.web.contoller;

import com.modusaleJava.server.ModusaleBatch;
import com.modusaleJava.server.utils.GpsData;
import com.modusaleJava.server.web.service.ImageService;
import com.modusaleJava.server.web.service.MainService;
import com.modusaleJava.server.web.service.TodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/getDataFromGps")
    public Map<String, List<String>> getDataWithGPS(){
        return mainService.getDataFrom();
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

