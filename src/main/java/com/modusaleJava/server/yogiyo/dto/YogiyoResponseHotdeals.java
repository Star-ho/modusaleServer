package com.modusaleJava.server.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YogiyoResponseHotdeals {
    @JsonProperty("additional_discount")
    private String additional_discount;

    @JsonProperty("name")
    private String name;

    @JsonProperty("franchise_id")
    private String franchise_id;

}
