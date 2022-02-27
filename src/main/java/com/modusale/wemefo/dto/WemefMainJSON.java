package com.modusale.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

public class WemefMainJSON {
    @JsonProperty("result")
    private Object result;

    @JsonProperty("data") @Getter
    private WemefJSON_2 data;

}
