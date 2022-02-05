package com.modusale.web.contoller;

import com.modusale.ModusaleBatch;
import com.modusale.utils.GpsData;
import com.modusale.web.service.ImageService;
import com.modusale.web.service.MainService;
import com.modusale.web.service.TodayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping
public class MainController {

    private final MainService mainService;
    private final ModusaleBatch modusaleBatch;
    private final ImageService imageService;
    private final TodayService todayService;

    public MainController(ImageService imageService,ModusaleBatch modusaleBatch,MainService mainService,TodayService todayService){
        this.imageService = imageService;
        this.modusaleBatch = modusaleBatch;
        this.mainService = mainService;
        this.todayService=todayService;
    }

    @GetMapping
    public Map<String, List<String>> getData(@RequestParam("ver")String version){
//        System.out.println(version);
        if(Objects.equals(version, "0.91")){
            throw new IllegalArgumentException("");
        }
        return mainService.getData();
    }

    @GetMapping("/getDataFromGps")
    public Map<String, List<String>> getDataWithGPS(@RequestParam String latitude,@RequestParam String longitude){
        return mainService.getDataFrom(new GpsData(latitude,longitude));
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

