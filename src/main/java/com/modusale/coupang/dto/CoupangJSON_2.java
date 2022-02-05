package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CoupangJSON_2 {
    @JsonProperty("mappedKeyword")
    private String mappedKeyword;

    @JsonProperty("nextToken")
    private String nextToken;

    @JsonProperty("entityList")
    private ArrayList<CoupangJSON_3> entityList;

}
