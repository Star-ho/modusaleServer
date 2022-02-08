package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

public class CoupangImageJSON_1 {
    @JsonProperty("titleEn")
    private  Object titleEn;
    @JsonProperty("title")
    private  Object title;
    @JsonProperty("images") @Getter
    private List<CoupangImageJSON_2> images;
}
