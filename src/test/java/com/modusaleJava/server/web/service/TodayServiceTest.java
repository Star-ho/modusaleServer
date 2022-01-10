package com.modusaleJava.server.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodayServiceTest {
    private TodayService todayService;

    @Autowired
    private void setTodayService(TodayService todayService){
        this.todayService=todayService;
    }

    @Test
    void showTodaySale() {
        todayService.showTodaySale();
    }
}