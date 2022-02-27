package com.modusale.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = {"job.autorun.enabled=false"})
@ExtendWith(SpringExtension.class)
class TelegramAPITest{

    private TelegramAPI telegramAPI;

    public TelegramAPITest(@Autowired TelegramAPI telegramAPI){
        this.telegramAPI=telegramAPI;
    }

    @Test
    public void sendTestMessage() throws InterruptedException {
        telegramAPI.send("test!");
        Thread.sleep(3000);
    }
}