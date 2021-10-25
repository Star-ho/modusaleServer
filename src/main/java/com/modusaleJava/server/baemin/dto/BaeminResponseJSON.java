package com.modusaleJava.server.baemin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BaeminResponseJSON {
    @JsonProperty("data")
    private List<BaeminResponseData> data;

    @JsonProperty("total")
    private int total;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;
}