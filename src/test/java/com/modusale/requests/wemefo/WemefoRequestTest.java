package com.modusale.requests.wemefo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusale.utils.AppDataObj;
import com.modusale.utils.GitHubData;
import com.modusale.utils.ModusaleRequest;
import com.modusale.utils.TelegramAPI;
import com.modusale.utils.properties.GithubProperty;
import com.modusale.utils.properties.WemefoProperty;
import com.modusale.requests.wemefo.dto.WemefMainJSON;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"job.autorun.enabled=false"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WemefoRequestTest {
    @Autowired
    private WemefoProperty wemefoProperty;
    @Autowired
    private TelegramAPI telegramAPI;
    @Autowired
    private AppDataObj appDataObj;
    @Autowired
    private GithubProperty githubProperty;
    @Value("${modusale.test.wemef.mainResponse}")
    private String wemefMainRes;
    @Value("${modusale.test.wemef.couponResponse}")
    private String wemefCouponRes;
    @Value("${modusale.test.wemef.githubResponse}")
    private String githubResponse;

    private final ObjectMapper objectMapper= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private GitHubData gitHubData;
    private ModusaleRequest modusaleRequest;

    @BeforeAll
    public void setup() throws JsonProcessingException {
        modusaleRequest = mock(ModusaleRequest.class);

        String URL=this.wemefoProperty.getURL();
        Map<String,String> headers = this.wemefoProperty.getHeaders();
        WemefMainJSON wemefMainJSON = objectMapper.readValue(this.wemefMainRes,WemefMainJSON.class);
        when(modusaleRequest.syncDataFrom(URL,headers, WemefMainJSON.class))
                .thenReturn(wemefMainJSON);
        when(modusaleRequest.syncDataFrom("https://www.wmpo.co.kr/events/1802337", String.class))
                .thenReturn(this.wemefCouponRes);
        when(modusaleRequest.syncDataFrom("https://raw.githubusercontent.com/Star-ho/modusaleDataFile/main/itemlistWemef",githubProperty.getHeader(),String.class))
                .thenReturn(githubResponse);

        gitHubData=new GitHubData(appDataObj,modusaleRequest,githubProperty);
    }

    @Test
    public void wemefoTest() {
        WemefoRequest wemefoRequest=new WemefoRequest(wemefoProperty, modusaleRequest,gitHubData,telegramAPI);
        var wemefOData = wemefoRequest.getWemefOData();

        assertEquals(22,wemefOData.size());
        assertEquals("부어치킨",wemefOData.get(0).getBrandName());
        assertEquals("3000",wemefOData.get(0).getPrice());
        assertEquals("치킨선생",wemefOData.get(7).getBrandName());
        assertEquals("3000",wemefOData.get(7).getPrice());
        assertEquals("빨간모자피자",wemefOData.get(12).getBrandName());
        assertEquals("4000",wemefOData.get(12).getPrice());
        assertEquals("CU",wemefOData.get(20).getBrandName());
        assertEquals("3000",wemefOData.get(20).getPrice());
    }

    @Test
    public void wemefoImageTest() {

        WemefoRequest wemefoRequest=new WemefoRequest(wemefoProperty, modusaleRequest,gitHubData,telegramAPI);
        var wemefOData = wemefoRequest.getWemefBannerList();

        assertEquals(35,wemefOData.size());
    }

}