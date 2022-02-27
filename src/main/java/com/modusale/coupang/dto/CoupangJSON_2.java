package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

public class CoupangJSON_2 {
    @JsonProperty("mappedKeyword")
    private String mappedKeyword;

    @JsonProperty("nextToken")
    private String nextToken;

    @JsonProperty("entityList")
    @Getter
    private ArrayList<CoupangJSON_3> entityList;

}
