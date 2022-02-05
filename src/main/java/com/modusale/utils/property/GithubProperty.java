package com.modusale.utils.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import java.util.Map;

@ConfigurationProperties("modusale.github")
@AllArgsConstructor
@ConstructorBinding
public class GithubProperty{
    @Getter
    private final String baseURL;
    @Getter private final String itemlistCoupang;
    @Getter private final String itemlistCoupangImage;
    @Getter private final String itemlistCoupangMonthly;
    @Getter private final String itemlistWemef;
    @Getter private final String menufile;
    @Getter private final String unifiedName;
    @Getter private final Map<String,String> header;
}