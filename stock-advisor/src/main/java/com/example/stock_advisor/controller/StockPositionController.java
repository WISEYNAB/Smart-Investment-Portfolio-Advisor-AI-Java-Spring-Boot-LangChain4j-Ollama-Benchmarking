package com.example.stock_advisor.controller;

import com.example.stock_advisor.model.StockHolding;
import com.example.stock_advisor.service.StockPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockPositionController {

    private final StockPositionService stockPositionService;

    @Autowired
    public StockPositionController(StockPositionService stockPositionService) {
        this.stockPositionService = stockPositionService;
    }

    @GetMapping("/stock-position/{stockSymbol}")
    public StockHolding getStockPosition(@PathVariable String stockSymbol) {
        return stockPositionService.getStockPositionSequential(stockSymbol);
    }
}