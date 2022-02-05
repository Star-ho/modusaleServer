package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CoupangJSON_5 {
    @JsonProperty("list")
    private ArrayList<CoupangJSON_6> list;

    @JsonProperty("info")
    private Object info;
}
