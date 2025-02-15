package com.entasis.trading.exception;

public class DeribitApiException extends RuntimeException {
    public DeribitApiException(String message) {
        super(message);
    }
    
    public DeribitApiException(String message, Throwable cause) {
        super(message, cause);
    }
} 