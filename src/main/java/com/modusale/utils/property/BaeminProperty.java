package com.modusale.utils.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties("modusale.baemin")
@ConstructorBinding
@AllArgsConstructor
public class BaeminProperty {
    @Getter private final String baeminSchemeHeader;
    @Getter private final String URL;
    @Getter private final List<String> category;
}