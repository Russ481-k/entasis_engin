package com.entasis.trading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

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
    @Qualifier("optionsWebClient")
    public WebClient optionsWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(5 * 1024 * 1024)) // 5MB로 증가
            .build();

        return WebClient.builder()
            .baseUrl("https://test.deribit.com")
            .exchangeStrategies(strategies)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
} 