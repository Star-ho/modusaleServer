package com.modusaleJava.server.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoupangImageJSON_2 {
    @JsonProperty("scheme")
    private String scheme;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;
}
