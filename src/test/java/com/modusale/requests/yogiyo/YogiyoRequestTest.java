package com.modusale.requests.yogiyo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusale.utils.GpsData;
import com.modusale.utils.ModusaleRequest;
import com.modusale.utils.properties.YogiyoProperty;
import com.modusale.requests.yogiyo.dto.YogiyoResponseJSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"job.autorun.enabled=false"})
class YogiyoRequestTest {

    @Value("${modusale.test.yogiyo.a}") private String yogiyoA;
    @Value("${modusale.test.yogiyo.b}") private String yogiyoB;
    @Value("${modusale.test.yogiyo.c}") private String yogiyoC;
    @Value("${modusale.test.yogiyo.d}") private String yogiyoD;

    @Autowired
    private YogiyoProperty yogiyoProperty;

    private final ObjectMapper objectMapper= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void yogiyoTest() throws JsonProcessingException {
        String loc="[37.4806211750226,126.944095160739],[37.496559,126.956980],[37.481185,126.997724],[37.557157,126.942628]";
        yogiyoProperty.setLocation(loc);
        Map<String,String> headers=yogiyoProperty.getHeaders();
        String aUrl="https://www.yogiyo.co.kr/api/v1/restaurants-geo/?order=rank&max_mov=&zip_code=151058&payment=all&max_delivery_fee=&home_category=all&use_hotdeal_v2=true&lng=126.944095160739&items=70&category=%EC%A0%84%EC%B2%B4&has_discount=&own_delivery_only=false&type=all&page=0&lat=37.4806211750226";
        String bUrl="https://www.yogiyo.co.kr/api/v1/restaurants-geo/?order=rank&max_mov=&zip_code=151058&payment=all&max_delivery_fee=&home_category=all&use_hotdeal_v2=true&lng=126.956980&items=70&category=%EC%A0%84%EC%B2%B4&has_discount=&own_delivery_only=false&type=all&page=0&lat=37.496559";
        String cUrl="https://www.yogiyo.co.kr/api/v1/restaurants-geo/?order=rank&max_mov=&zip_code=151058&payment=all&max_delivery_fee=&home_category=all&use_hotdeal_v2=true&lng=126.997724&items=70&category=%EC%A0%84%EC%B2%B4&has_discount=&own_delivery_only=false&type=all&page=0&lat=37.481185";
        String dUrl="https://www.yogiyo.co.kr/api/v1/restaurants-geo/?order=rank&max_mov=&zip_code=151058&payment=all&max_delivery_fee=&home_category=all&use_hotdeal_v2=true&lng=126.942628&items=70&category=%EC%A0%84%EC%B2%B4&has_discount=&own_delivery_only=false&type=all&page=0&lat=37.557157";

        ModusaleRequest modusaleRequest = mock(ModusaleRequest.class);

        YogiyoResponseJSON yogiyoA = objectMapper.readValue(this.yogiyoA,YogiyoResponseJSON.class);
        YogiyoResponseJSON yogiyoB = objectMapper.readValue(this.yogiyoB,YogiyoResponseJSON.class);
        YogiyoResponseJSON yogiyoC = objectMapper.readValue(this.yogiyoC,YogiyoResponseJSON.class);
        YogiyoResponseJSON yogiyoD = objectMapper.readValue(this.yogiyoD,YogiyoResponseJSON.class);

        when(modusaleRequest.syncDataListFrom(List.of(aUrl,bUrl,cUrl,dUrl),headers, YogiyoResponseJSON.class))
                .thenReturn(List.of(yogiyoA,yogiyoB,yogiyoC,yogiyoD));

        YogiyoRequest yogiyoRequest=new YogiyoRequest(yogiyoProperty,modusaleRequest);

        var a = yogiyoRequest.getAppData();
        assertEquals(53,a.size());
        assertEquals("????????????",a.get(0).getBrandName());
        assertEquals("3000",a.get(0).getPrice());
        assertEquals("????????????",a.get(21).getBrandName());
        assertEquals("4000",a.get(21).getPrice());
        assertEquals("?????????",a.get(38).getBrandName());
        assertEquals("10000",a.get(38).getPrice());
        assertEquals("????????????",a.get(52).getBrandName());
        assertEquals("4000",a.get(52).getPrice());
    }

    @Test
    public void yogiyoGPSTest() throws JsonProcessingException {
        GpsData gpsData = new GpsData("37.4806211750226","126.944095160739");
        Map<String,String> headers=yogiyoProperty.getHeaders();
        String aUrl="https://www.yogiyo.co.kr/api/v1/restaurants-geo/?order=rank&max_mov=&zip_code=151058&payment=all&max_delivery_fee=&home_category=all&use_hotdeal_v2=true&lng=126.944095160739&items=70&category=%EC%A0%84%EC%B2%B4&has_discount=&own_delivery_only=false&type=all&page=0&lat=37.4806211750226";

        ModusaleRequest modusaleRequest = mock(ModusaleRequest.class);

        YogiyoResponseJSON yogiyoA = objectMapper.readValue(this.yogiyoA,YogiyoResponseJSON.class);

        when(modusaleRequest.syncDataFrom(aUrl,headers, YogiyoResponseJSON.class))
                .thenReturn(yogiyoA);

        YogiyoRequest yogiyoRequest=new YogiyoRequest(yogiyoProperty,modusaleRequest);

        var a = yogiyoRequest.getAppDataByGps(gpsData);

        assertEquals(37,a.size());
        assertEquals("??????????????????",a.get(0).getBrandName());
        assertEquals("4000",a.get(0).getPrice());
        assertEquals("????????????",a.get(12).getBrandName());
        assertEquals("3000",a.get(12).getPrice());
        assertEquals("????????????",a.get(22).getBrandName());
        assertEquals("2000",a.get(22).getPrice());
        assertEquals("????????????",a.get(32).getBrandName());
        assertEquals("3000",a.get(32).getPrice());
    }
}