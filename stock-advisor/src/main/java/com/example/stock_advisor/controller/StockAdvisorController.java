package com.example.stock_advisor.controller;

import com.example.stock_advisor.assistant.StockAdvisorAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockAdvisorController {


    private final StockAdvisorAssistant assistant;

    public StockAdvisorController(StockAdvisorAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/chat")
    public String chat(String userMessage) {
        return assistant.chat(userMessage);
    }
}