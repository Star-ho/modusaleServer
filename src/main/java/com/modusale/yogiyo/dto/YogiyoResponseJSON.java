package com.modusale.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class YogiyoResponseJSON {
    @JsonProperty("pagination")
    private Object pagination;

    @JsonProperty("hotdeals") @Getter
    private YogiyoResponseItems hotdeals;

    @JsonProperty("restaurants")
    private Object restaurants;
}
