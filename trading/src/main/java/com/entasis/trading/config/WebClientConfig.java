package com.entasis.trading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @Bean
    public WebClient optionsWebClient() {
        return WebClient.builder()
            .baseUrl("https://test.deribit.com") // 또는 "https://www.deribit.com" for production
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
} 