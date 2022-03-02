package com.modusale.requests.baemin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class BaeminResponseJSON {
    @JsonProperty("data")
    @Getter
    private List<BaeminResponseData> data;

    @JsonProperty("total")
    private int total;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;
}