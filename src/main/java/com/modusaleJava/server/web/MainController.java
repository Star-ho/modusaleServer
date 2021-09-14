package com.modusaleJava.server.web;

import com.modusaleJava.server.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class MainController {

    private MainService mainService;

    @Autowired
    public void setMainService(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public Map<String, List<String>> returnData(){
        return mainService.getData();
    }

    @GetMapping("refresh")
    public String refreshData(){
        mainService.refresh();
        return "refresh!";
    }
}

