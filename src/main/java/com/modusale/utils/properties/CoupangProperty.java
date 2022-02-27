package com.modusale.utils.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import java.util.Map;

@ConfigurationProperties("modusale.coupang")
@ConstructorBinding
@AllArgsConstructor
public class CoupangProperty{
    @Getter private final String URL;
    @Getter private final Map<String,String> header;
}