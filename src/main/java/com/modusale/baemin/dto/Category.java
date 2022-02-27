package com.modusale.baemin.dto;

import lombok.Getter;

public class Category{
    @Getter
    private String category;
    private Boolean isNotEmpty;
    public Category(String category, Boolean isNotEmpty){
        this.category=category;
        this.isNotEmpty=isNotEmpty;
    }
}

