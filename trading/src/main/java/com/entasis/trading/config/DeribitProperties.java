package com.entasis.trading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "deribit")
public class DeribitProperties {
    private String baseUrl;
    private String apiKey;
    private String secretKey;
} 