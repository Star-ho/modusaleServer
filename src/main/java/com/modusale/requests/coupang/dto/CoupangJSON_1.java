package com.modusale.requests.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class CoupangJSON_1 {
    @JsonProperty("data")
    @Getter
    private CoupangJSON_2 data;

    @JsonProperty("error")
    private Object error;
}
