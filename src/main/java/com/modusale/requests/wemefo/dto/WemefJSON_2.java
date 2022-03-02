package com.modusale.requests.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

public class WemefJSON_2 {
    @JsonProperty("templates") @Getter
    private ArrayList<WemefJSON_3> templates;

    @JsonProperty("footer")
    private Object footer;

    @JsonProperty("requireData")
    private Object requireData;
}
