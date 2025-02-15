package com.entasis.trading.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectorRequest {
    private List<String> symbols;
    private List<String> currencies;
    private List<String> exchanges;
} 