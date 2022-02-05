package com.modusale.utils.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import java.util.Map;

@ConfigurationProperties("modusale.wemefo")
@ConstructorBinding
@AllArgsConstructor
public class WemefoProperty {
    @Getter private final String URL;
    @Getter private final Map<String,String> headers;
}
