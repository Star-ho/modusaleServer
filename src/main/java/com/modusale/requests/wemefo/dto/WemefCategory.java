package com.modusale.requests.wemefo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class WemefCategory {
    @JsonProperty("title") @Getter
    private String title;

    @JsonProperty("link") @Getter
    private String link;

    @JsonProperty("image")
    private String image;

    @JsonProperty("item")
    private Object item;
}
