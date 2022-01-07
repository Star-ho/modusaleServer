package com.modusaleJava.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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