package com.modusale.baemin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusale.baemin.dto.BaeminResponseJSON;
import com.modusale.utils.ModusaleRequest;
import com.modusale.utils.properties.BaeminProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(properties = {"job.autorun.enabled=false"})
class BaeminRequestTest {

    @Autowired
    private BaeminProperty baeminProperty;


    @Value("${modusale.test.baemin.FASTFOOD.a}") private String fastFoodA;
    @Value("${modusale.test.baemin.FASTFOOD.b}") private String fastFoodB;
    @Value("${modusale.test.baemin.CAFE.a}") private String cafeA;
    @Value("${modusale.test.baemin.CAFE.b}") private String cafeB;
    @Value("${modusale.test.baemin.CAFE.c}") private String cafeC;
    @Value("${modusale.test.baemin.PIZZA.a}") private String pizzaA;
    @Value("${modusale.test.baemin.PIZZA.b}") private String pizzaB;
    @Value("${modusale.test.baemin.PIZZA.c}") private String pizzaC;
    @Value("${modusale.test.baemin.PIZZA.d}") private String pizzaD;
    @Value("${modusale.test.baemin.PIZZA.e}") private String pizzaE;

    @Test
    public void baeminTest() throws JsonProcessingException {
        ObjectMapper objectMapper= new ObjectMapper();
        List<String> category= Arrays.asList("FASTFOOD","CAFE","PIZZA");
        this.baeminProperty.setCategories(category);
        BaeminResponseJSON fastFoodA = objectMapper.readValue(this.fastFoodA,BaeminResponseJSON.class);
        BaeminResponseJSON fastFoodB = objectMapper.readValue(this.fastFoodB,BaeminResponseJSON.class);
        BaeminResponseJSON cafeA = objectMapper.readValue(this.cafeA,BaeminResponseJSON.class);
        BaeminResponseJSON cafeB = objectMapper.readValue(this.cafeB,BaeminResponseJSON.class);
        BaeminResponseJSON cafeC = objectMapper.readValue(this.cafeC,BaeminResponseJSON.class);
        BaeminResponseJSON pizzaA = objectMapper.readValue(this.pizzaA,BaeminResponseJSON.class);
        BaeminResponseJSON pizzaB = objectMapper.readValue(this.pizzaB,BaeminResponseJSON.class);
        BaeminResponseJSON pizzaC = objectMapper.readValue(this.pizzaC,BaeminResponseJSON.class);
        BaeminResponseJSON pizzaD = objectMapper.readValue(this.pizzaD,BaeminResponseJSON.class);
        BaeminResponseJSON pizzaE = objectMapper.readValue(this.pizzaE,BaeminResponseJSON.class);

        ModusaleRequest modusaleRequest = mock(ModusaleRequest.class);
        when(modusaleRequest.asyncSend(List.of("https://lounge.baemin.com/api/lounge/brands/cards/FASTFOOD?pageNumber=0&pageSize=10","https://lounge.baemin.com/api/lounge/brands/cards/CAFE?pageNumber=0&pageSize=10","https://lounge.baemin.com/api/lounge/brands/cards/PIZZA?pageNumber=0&pageSize=10"), BaeminResponseJSON.class))
                .thenReturn(List.of(fastFoodA,cafeA,pizzaA));
        when(modusaleRequest.asyncSend(List.of("https://lounge.baemin.com/api/lounge/brands/cards/FASTFOOD?pageNumber=1&pageSize=10","https://lounge.baemin.com/api/lounge/brands/cards/CAFE?pageNumber=1&pageSize=10","https://lounge.baemin.com/api/lounge/brands/cards/PIZZA?pageNumber=1&pageSize=10"), BaeminResponseJSON.class))
                .thenReturn(List.of(fastFoodB,cafeB,pizzaB));
        when(modusaleRequest.asyncSend(List.of("https://lounge.baemin.com/api/lounge/brands/cards/CAFE?pageNumber=2&pageSize=10","https://lounge.baemin.com/api/lounge/brands/cards/PIZZA?pageNumber=2&pageSize=10"), BaeminResponseJSON.class))
                .thenReturn(List.of(cafeC,pizzaC));
        when(modusaleRequest.asyncSend(List.of("https://lounge.baemin.com/api/lounge/brands/cards/PIZZA?pageNumber=3&pageSize=10"), BaeminResponseJSON.class))
                .thenReturn(List.of(pizzaD));
        when(modusaleRequest.asyncSend(List.of("https://lounge.baemin.com/api/lounge/brands/cards/PIZZA?pageNumber=4&pageSize=10"), BaeminResponseJSON.class))
                .thenReturn(List.of(pizzaE));

        BaeminRequest baeminRequest= new BaeminRequest(baeminProperty, modusaleRequest);

        var modusaleAppDataList = baeminRequest.getAppData();
        assertEquals(8,modusaleAppDataList.size());
    }

}