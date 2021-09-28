package com.modusaleJava.server;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class ModusaleAppData {
    private String appName;
    private String brandName;
    private Integer price;
    private String brandScheme;
}