package com.modusale.requests.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class CoupangJSON_3 {
    @JsonProperty("entity")
    @Getter
    private CoupangJSON_4 entity;

    @JsonProperty("viewType")
    private String viewType;
}
