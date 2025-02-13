package com.entasis.trading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient spotWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.binance.com")
            .build();
    }
    
    @Bean
    public WebClient futuresWebClient() {
        return WebClient.builder()
            .baseUrl("https://fapi.binance.com")  // 선물용 URL
            .build();
    }
} 