package com.modusaleJava.server.baemin;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaeminRequestTest {
    @Autowired
    private BaeminRequest baeminRequest;

    @Test
    public void baeminTest(){
        baeminRequest.getAppData();
    }



}