package com.modusale.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class WemefJSON_2 {
    @JsonProperty("templates") @Getter
    private ArrayList<WemefJSON_3> templates;

    @JsonProperty("footer")
    private Object footer;

    @JsonProperty("requireData")
    private Object requireData;
}
