package com.modusale.coupang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusale.baemin.dto.BaeminResponseJSON;
import com.modusale.coupang.dto.CoupangJSON_1;
import com.modusale.utils.*;
import com.modusale.utils.properties.CoupangProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @Value("${modusale.test.coupang.banner}") private String banner;
    @Value("${modusale.test.coupang.monthlyRes}") private String monthlyRes;

    @Test
    public void coupangTest() throws JsonProcessingException {
        ObjectMapper objectMapper= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String URL=coupangProperty.getURL();
        Map<String,String> headers=coupangProperty.getHeader();
        ModusaleRequest modusaleRequest = mock(ModusaleRequest.class);
        CoupangJSON_1 banner = objectMapper.readValue(this.banner,CoupangJSON_1.class);

        when(modusaleRequest.syncDataFrom(URL, headers, CoupangJSON_1.class))
                .thenReturn(banner);
        when(modusaleRequest.syncDataFrom("https://web.coupangeats.com/customer/landingPage?key=FEB_0207_IN", String.class))
                .thenReturn(monthlyRes);
        CoupangRequest coupangRequest=new CoupangRequest(telegramAPI,gitHubData,modusaleMapper,modusaleRequest,coupangProperty);

        var a = coupangRequest.getAppData();
        assertEquals(24,a.size());
    }

    @Test
    public void coupangImageTest(){
        CoupangRequest coupangRequest=new CoupangRequest(telegramAPI,gitHubData,modusaleMapper,modusaleRequest,coupangProperty);

        var a = coupangRequest.getCoupangBannerImage();
        assertEquals(33,a.size());
    }

    @Test
    public void coupangGpsTest(){
        CoupangRequest coupangRequest=new CoupangRequest(telegramAPI,gitHubData,modusaleMapper,modusaleRequest,coupangProperty);
        GpsData gpsData = new GpsData("37.4806211750226","126.944095160739");
        var a = coupangRequest.getAppDataBy(gpsData);
        assertEquals(24,a.size());
    }
}