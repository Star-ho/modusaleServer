package com.modusale.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class YogiyoResponseItems {

    @JsonProperty("title")
    private String title;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("items")
    private ArrayList<YogiyoResponseHotdeals> items;
}



