package com.modusale.coupang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoupangJSON_6 {
    @JsonProperty("id")
    private String id;

    @JsonProperty("scheme")
    private String scheme;

    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("documentKey")
    private String documentKey;

    @JsonProperty("expirationDateText")
    private String expirationDateText;
}
