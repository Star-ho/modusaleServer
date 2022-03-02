package com.modusale.requests.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class CoupangJSON_6 {
    @JsonProperty("id")
    @Getter
    private String id;

    @JsonProperty("scheme")
    @Getter
    private String scheme;

    @JsonProperty("imagePath")
    @Getter
    private String imagePath;

    @JsonProperty("documentKey")
    private String documentKey;

    @JsonProperty("expirationDateText")
    private String expirationDateText;

}
