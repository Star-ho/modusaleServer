package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

public class CoupangJSON_5 {
    @JsonProperty("list")
    @Getter
    private ArrayList<CoupangJSON_6> list;

    @JsonProperty("info")
    private Object info;

    @JsonProperty("shortcuts")
    private Object shortcuts;

    @JsonProperty("height")
    private Object height;

    @JsonProperty("color")
    private Object color;

}
