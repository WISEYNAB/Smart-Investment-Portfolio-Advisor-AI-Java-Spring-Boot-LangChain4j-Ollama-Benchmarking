package com.example.stock_advisor.controller;

import com.example.stock_advisor.assistant.StockAdvisorAssistant;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
public class StockAdvisorController {

    private final StockAdvisorAssistant assistant;

    public StockAdvisorController(StockAdvisorAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String userMessage) {
        try {
            String response = assistant.chat(userMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // logs full stacktrace in console
            return ResponseEntity.internalServerError()
                    .body("Error occurred in chat: " + e.getMessage());
        }
    }

    // Fallback handler for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError()
                .body("Unexpected error: " + e.getMessage());
    }
}
