package com.modusale.coupang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.modusale.utils.GitHubData;
import com.modusale.utils.ModusaleMapper;
import com.modusale.utils.ModusaleRequest;
import com.modusale.utils.TelegramAPI;
import com.modusale.utils.properties.CoupangProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"job.autorun.enabled=false"})
class CoupangRequestTest {
    @Autowired
    private TelegramAPI telegramAPI;
    @Autowired
    private GitHubData gitHubData;
    @Autowired
    private ModusaleMapper modusaleMapper;
    @Autowired
    private ModusaleRequest modusaleRequest;
    @Autowired
    private CoupangProperty coupangProperty;

    @Test
    public void coupangTest() throws JsonProcessingException {
        CoupangRequest coupangRequest=new CoupangRequest(telegramAPI,gitHubData,modusaleMapper,modusaleRequest,coupangProperty);

        var a = coupangRequest.getAppData();
        assertEquals(24,a.size());
    }
}