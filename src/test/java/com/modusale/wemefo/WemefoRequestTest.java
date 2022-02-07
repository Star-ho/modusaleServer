package com.modusale.wemefo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusale.baemin.dto.BaeminResponseJSON;
import com.modusale.utils.GitHubData;
import com.modusale.utils.ModusaleRequestTemplate;
import com.modusale.utils.TelegramAPI;
import com.modusale.utils.properties.WemefoProperty;
import com.modusale.wemefo.dto.WemefMainJSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"job.autorun.enabled=false"})
class WemefoRequestTest {
    @Autowired
    private WemefoProperty wemefoProperty;
    @Autowired
    private GitHubData gitHubData;
    @Autowired
    private TelegramAPI telegramAPI;
    @Value("${modusale.test.wemef.mainResponse}")
    private String wemefMainRes;
    @Value("${modusale.test.wemef.couponResponse}")
    private String wemefCouponRes;

    @Test
    public void wemefoTest() throws JsonProcessingException {
        ModusaleRequestTemplate modusaleRequestTemplate = mock(ModusaleRequestTemplate.class);
        ObjectMapper objectMapper= new ObjectMapper();

        String URL=this.wemefoProperty.getURL();
        Map<String,String> headers = this.wemefoProperty.getHeaders();
        WemefMainJSON wemefMainJSON = objectMapper.readValue(this.wemefMainRes,WemefMainJSON.class);
        when(modusaleRequestTemplate.syncDataFrom(URL,headers, WemefMainJSON.class))
                .thenReturn(wemefMainJSON);
        when(modusaleRequestTemplate.syncDataFrom("https://www.wmpo.co.kr/events/1802337", String.class))
                .thenReturn(this.wemefCouponRes);
        WemefoRequest wemefoRequest=new WemefoRequest(wemefoProperty,modusaleRequestTemplate,gitHubData,telegramAPI);
        var wemefOData = wemefoRequest.getWemefOData();
        assertEquals(22,wemefOData.size());
    }
}