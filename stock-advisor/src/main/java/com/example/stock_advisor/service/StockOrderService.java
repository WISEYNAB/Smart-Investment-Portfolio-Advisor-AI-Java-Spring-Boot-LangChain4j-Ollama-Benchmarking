package com.example.stock_advisor.service;

import com.example.stock_advisor.exception.NoDataFoundException;
import com.example.stock_advisor.model.OrderType;
import com.example.stock_advisor.model.StockHoldingDetails;
import com.example.stock_advisor.model.StockOrder;
import com.example.stock_advisor.repository.StockOrderRepository;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class StockOrderService {

    private static final Logger log = LoggerFactory.getLogger(StockOrderService.class);

    private final StockOrderRepository stockOrderRepository;

    @Autowired
    public StockOrderService(StockOrderRepository stockOrderRepository) {
        this.stockOrderRepository = stockOrderRepository;
    }

    @Tool
    public StockOrder createOrder(StockOrder order) {
        StockOrder newOrder = new StockOrder(
                null,
                "testuser",
                order.symbol(),
                order.quantity(),
                order.price(),
                order.orderType(),
                LocalDateTime.now()
        );
        return stockOrderRepository.save(newOrder);
    }

    @Tool
    public List<StockOrder> getAllOrders() {
        return stockOrderRepository.findAll();
    }

    public StockOrder getOrderById(Long id) {
        return stockOrderRepository.findById(id).orElse(null);
    }

    public List<StockOrder> getOrdersBySymbol(String symbol) {
        log.info("getOrdersBySymbol.currentThread: {}", Thread.currentThread());
        var orders = stockOrderRepository.findBySymbol(symbol);
        if(orders.isEmpty()) {
            throw new NoDataFoundException();
        }
        return orders;
    }

    @Async
    public CompletableFuture<List<StockOrder>> getOrdersBySymbolAsync(String symbol) {
        log.info("getOrdersBySymbolAsync.currentThread: {}", Thread.currentThread());
        var orders = stockOrderRepository.findBySymbol(symbol);
        if(orders.isEmpty()) {
            throw new NoDataFoundException();
        }
        return CompletableFuture.completedFuture(orders);
    }

    @Tool
    public List<StockHoldingDetails> getStockHoldingDetails() {
        return stockOrderRepository.findAll().stream()
                .collect(Collectors.groupingBy(StockOrder::symbol, Collectors.summingDouble(order ->
                        order.orderType() == OrderType.BUY ? order.quantity() : -order.quantity())))
                .entrySet().stream()
                .map(entry -> new StockHoldingDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}