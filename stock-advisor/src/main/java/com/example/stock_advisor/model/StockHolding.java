package com.example.stock_advisor.model;

public record StockHolding(
        String stockSymbol,
        double quantity,
        double currentMarketValue,
        double currentMarketPrice,
        String companyName,
        String industry,
        String website,
        String sector
) {
}