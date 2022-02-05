package com.modusale;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class ModusaleAppData {
    protected String appName;
    protected String brandName;
    protected String price;
    protected String brandScheme;
    protected String id;
    protected String imagePath;
}
