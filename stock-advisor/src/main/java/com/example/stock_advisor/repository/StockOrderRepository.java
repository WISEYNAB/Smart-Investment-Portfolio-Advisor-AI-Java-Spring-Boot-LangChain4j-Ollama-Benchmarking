package com.example.stock_advisor.repository;

import com.example.stock_advisor.model.StockOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockOrderRepository extends ListCrudRepository<StockOrder, Long> {
    List<StockOrder> findBySymbol(String symbol);
}