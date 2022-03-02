package com.modusale.requests.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class YogiyoResponseJSON {
    @JsonProperty("pagination")
    private Object pagination;

    @JsonProperty("hotdeals") @Getter
    private YogiyoResponseItems hotdeals;

    @JsonProperty("restaurants")
    private Object restaurants;
}
