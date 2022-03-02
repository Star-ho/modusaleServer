package com.modusale.requests.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class CoupangImageJSON_2 {
    @JsonProperty("scheme")
    @Getter @Setter
    private String scheme;

    @JsonProperty("imageUrl")
    @Getter
    private String imageUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;
}
