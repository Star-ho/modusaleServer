package com.modusale.utils.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;
@ConfigurationProperties("modusale.yogiyo")
@ConstructorBinding
public class YogiyoProperty {
    @Getter private final String URL;
    @Getter @Setter private final Map<String,String> headers;
    @Getter @Setter private String location;

    public YogiyoProperty(String URL, Map<String, String > headers, String location){
        this.URL=URL;
        this.headers=headers;
        this.location=location;
    }
}
