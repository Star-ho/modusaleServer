package com.modusale.utils.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("modusale.telegram")
@ConstructorBinding
@AllArgsConstructor
public class TelegramProperty{
    @Getter private final String sendURL;
    @Getter private final String deleteURL;

}
