package com.modusale;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
public class ModusaleAppData {
    @Getter @Setter
    protected String appName;
    @Getter @Setter
    protected String brandName;
    @Getter @Setter
    protected String price;
    @Getter @Setter
    protected String brandScheme;
    @Setter
    protected String id;
    @Setter
    protected String imagePath;
}
