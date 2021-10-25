package com.modusaleJava.server.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YogiyoResponseJSON {
    @JsonProperty("pagination")
    private Object pagination;

    @JsonProperty("hotdeals")
    private YogiyoResponseItems hotdeals;

    @JsonProperty("restaurants")
    private Object restaurants;
}
