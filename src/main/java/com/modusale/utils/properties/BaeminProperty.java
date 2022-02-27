package com.modusale.utils.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties("modusale.baemin")
@ConstructorBinding
@AllArgsConstructor
public class BaeminProperty {
    @Getter private final String appName;
    @Getter private final String baeminSchemeHeader;
    @Getter private final String URL;
    @Getter @Setter private List<String> categories;
}