package com.modusale.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

public class YogiyoResponseItems {

    @JsonProperty("title")
    private String title;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("items")
    @Getter
    private ArrayList<YogiyoResponseHotdeals> items;
}



