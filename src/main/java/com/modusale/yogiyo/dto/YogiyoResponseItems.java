package com.modusale.yogiyo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class YogiyoResponseItems {
    @JsonProperty("items")
    private ArrayList<YogiyoResponseHotdeals> items;

}



