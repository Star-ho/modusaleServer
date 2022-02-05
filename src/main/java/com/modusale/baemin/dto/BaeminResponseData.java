package com.modusale.baemin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaeminResponseData {
    @JsonProperty("id")
    private int id;

    @JsonProperty("brandId")
    private int brandId;

    @JsonProperty("brandName")
    private String brandName;

    @JsonProperty("logoUrl")
    private String logoUrl;

    @JsonProperty("description")
    private Object description;

    @JsonProperty("descriptionMenu")
    private Object descriptionMenu;

    @JsonProperty("backgroundImage")
    private Object backgroundImage;

    @JsonProperty("backgroundColor")
    private Object backgroundColor;

    @JsonProperty("hasMenus")
    private Object hasMenus;

    @JsonProperty("brandContractType")
    private String brandContractType;

    @JsonProperty("brandMarker")
    private String brandMarker;

    @JsonProperty("backgroundImageUrl")
    private Object backgroundImageUrl;

    @JsonProperty("eventBanners")
    private Object eventBanners;

    @JsonProperty("menuBanners")
    private Object menuBanners;

    @JsonProperty("maxDiscountCouponPrice")
    private String maxDiscountCouponPrice;
}
