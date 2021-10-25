package com.modusaleJava.server.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WemefJSON_3 {
    @JsonProperty("template")
    private Integer template;

    @JsonProperty("type")
    private String type;

    @JsonProperty("key")
    private String key;

    @JsonProperty("ID")
    private String ID;

    @JsonProperty("title")
    private String title;

    @JsonProperty("subTitle")
    private String subTitle;

    @JsonProperty("image")
    private String image;

    @JsonProperty("link")
    private String link;

    @JsonProperty("items")
    private ArrayList<WemefJSON_4> items;

}
