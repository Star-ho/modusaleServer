package com.modusale.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> illegalHandler(){
        var a = new HashMap<String,String>();
        a.put("error","error");
        return new ResponseEntity(a, HttpStatus.ACCEPTED);
    }
}
