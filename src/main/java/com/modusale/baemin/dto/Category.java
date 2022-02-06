package com.modusale.baemin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Category{
    @Getter
    private String category;
    @Getter @Setter
    private Boolean isNotEmpty;
    public Category(String category, Boolean isNotEmpty){
        this.category=category;
        this.isNotEmpty=isNotEmpty;
    }
}

