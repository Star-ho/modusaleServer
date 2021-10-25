package com.modusaleJava.server.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WemefJSON_2 {
    @JsonProperty("templates")
    private ArrayList<WemefJSON_3> templates;

    @JsonProperty("footer")
    private Object footer;

    @JsonProperty("requireData")
    private Object requireData;
}
