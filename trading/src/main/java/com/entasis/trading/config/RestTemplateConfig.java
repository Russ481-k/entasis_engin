package com.entasis.trading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import com.entasis.trading.exception.DeribitApiException;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Add error handler
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode().isError();
            }
            
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                throw new DeribitApiException(
                    "API error: " + response.getStatusCode() + " " + response.getStatusText()
                );
            }
        });
        
        return restTemplate;
    }
} 