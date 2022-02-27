package com.modusale.coupang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusale.coupang.dto.CoupangJSON_1;
import com.modusale.utils.*;
import com.modusale.utils.properties.CoupangProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

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
    private CoupangProperty coupangProperty;
    private final ObjectMapper objectMapper= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Value("${modusale.test.coupang.banner}") private String banner;
    @Value("${modusale.test.coupang.gpsBanner}") private String gpsBanner;
    @Value("${modusale.test.coupang.monthlyRes}") private String monthlyRes;

    @Test
    public void coupangTest() throws JsonProcessingException {
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
    public void coupangImageTest() throws JsonProcessingException {
        String URL=coupangProperty.getURL();
        Map<String,String> headers=coupangProperty.getHeader();
        ModusaleRequest modusaleRequest = mock(ModusaleRequest.class);
        CoupangJSON_1 banner = objectMapper.readValue(this.banner,CoupangJSON_1.class);

        when(modusaleRequest.syncDataFrom(URL, headers, CoupangJSON_1.class))
                .thenReturn(banner);
        when(modusaleRequest.syncDataFrom("https://web.coupangeats.com/customer/landingPage?key=FEB_0207_IN", String.class))
                .thenReturn(monthlyRes);
        CoupangRequest coupangRequest=new CoupangRequest(telegramAPI,gitHubData,modusaleMapper,modusaleRequest,coupangProperty);

        var a = coupangRequest.getCoupangBannerImage();
        assertEquals(33,a.size());
    }

    @Test
    public void coupangGpsTest() throws JsonProcessingException {
        String URL=coupangProperty.getURL();
        GpsData gpsData = new GpsData("35.195457","129.038147");
        Map<String,String> headers=coupangProperty.getHeader();
        headers.put("X-EATS-LOCATION", "{\"addressId\":0,\"latitude\":"+gpsData.getLatitude()+",\"longitude\":"+gpsData.getLongitude()+",\"regionId\":10,\"siDo\":\"%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C\",\"siGunGu\":\"%EC%A4%91%EA%B5%AC\"}");
        ModusaleRequest modusaleRequest = mock(ModusaleRequest.class);
        CoupangJSON_1 banner = objectMapper.readValue(this.gpsBanner,CoupangJSON_1.class);

        when(modusaleRequest.syncDataFrom(URL, headers, CoupangJSON_1.class))
                .thenReturn(banner);
        when(modusaleRequest.syncDataFrom("https://web.coupangeats.com/customer/landingPage?key=FEB_0207_IN", String.class))
                .thenReturn(monthlyRes);
        CoupangRequest coupangRequest=new CoupangRequest(telegramAPI,gitHubData,modusaleMapper,modusaleRequest,coupangProperty);

        var a = coupangRequest.getAppDataBy(gpsData);
        assertEquals(23,a.size());
    }
}