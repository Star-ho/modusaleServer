package com.modusaleJava.server.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WemefJSON_4 {
    @JsonProperty("title")
    private String title;

    @JsonProperty("link")
    private String link;

    @JsonProperty("image")
    private String image;

    @JsonProperty("item")
    private Object item;
}
