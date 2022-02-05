package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoupangJSON_1 {
    @JsonProperty("data")
    private CoupangJSON_2 data;

    @JsonProperty("error")
    private Object error;
}
