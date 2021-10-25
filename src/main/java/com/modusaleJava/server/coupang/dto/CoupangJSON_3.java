package com.modusaleJava.server.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoupangJSON_3 {
    @JsonProperty("entity")
    private CoupangJSON_4 entity;

    @JsonProperty("viewType")
    private String viewType;
}
