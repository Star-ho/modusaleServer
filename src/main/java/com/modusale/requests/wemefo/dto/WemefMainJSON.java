package com.modusale.requests.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class WemefMainJSON {
    @JsonProperty("result")
    private Object result;

    @JsonProperty("data") @Getter
    private WemefJSON_2 data;

}
