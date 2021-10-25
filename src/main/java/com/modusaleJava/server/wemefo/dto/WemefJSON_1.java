package com.modusaleJava.server.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WemefJSON_1 {
    @JsonProperty("result")
    private Object result;

    @JsonProperty("data")
    private WemefJSON_2 data;

}
