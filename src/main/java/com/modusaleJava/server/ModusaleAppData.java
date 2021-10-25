package com.modusaleJava.server;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class ModusaleAppData {
    private String appName;
    private String brandName;
    private String price;
    private String brandScheme;
    private String id;
    private String imagePath;
}
