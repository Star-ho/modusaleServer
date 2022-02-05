package com.modusale.utils.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;
@ConfigurationProperties("modusale.yogiyo")
@ConstructorBinding
public class YogiyoProperty {
    @Getter private final String URL;
    @Getter private final Map<String,String> headers;
    @Getter private final String location;

    public YogiyoProperty(String URL, Map<String, String > headers, String location){
        this.URL=URL;
        this.headers=headers;
        this.location=location;
    }
}
