package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoupangImageJSON_1 {
    @JsonProperty("titleEn")
    private  Object titleEn;
    @JsonProperty("title")
    private  Object title;
    @JsonProperty("images")
    private List<CoupangImageJSON_2> images;
}
