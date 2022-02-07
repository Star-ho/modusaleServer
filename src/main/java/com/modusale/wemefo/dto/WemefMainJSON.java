package com.modusale.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WemefMainJSON {
    @JsonProperty("result")
    private Object result;

    @JsonProperty("data")
    private WemefJSON_2 data;

}
