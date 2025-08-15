package com.example.stock_advisor.service;

import com.example.stock_advisor.config.StockAPIConfig;
import com.example.stock_advisor.model.CompanyDetails;
import com.example.stock_advisor.model.StockPrice;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor

public class StockInformationService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StockInformationService.class);

    private final StockAPIConfig stockAPIConfig;
    private final RestClient restClient;

    @Tool("Returns the stock price for the given stock symbols")
    public String getStockPrice(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching stock price | Symbols: {}", stockSymbols);
        return fetchData("/quote/" + stockSymbols);
    }

    @Tool("Returns the company profile for the given stock symbols")
    public String getCompanyProfile(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching company profile | Symbols: {}", stockSymbols);
        return fetchData("/profile/" + stockSymbols);
    }

    @Tool("Returns the balance sheet statements for the given stock symbols")
    public List<String> getBalanceSheetStatements(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching balance sheet statements | Symbols: {}", stockSymbols);
        return fetchDataForMultipleSymbols(stockSymbols, "/balance-sheet-statement/");
    }

    @Tool("Returns the income statements for the given stock symbols")
    public List<String> getIncomeStatements(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching income statements | Symbols: {}", stockSymbols);
        return fetchDataForMultipleSymbols(stockSymbols, "/income-statement/");
    }

    @Tool("Returns the cash flow statements for the given stock symbols")
    public List<String> getCashFlowStatements(@P("Stock symbols separated by ,") String stockSymbols) {
        log.info("Fetching cash flow statements | Symbols: {}", stockSymbols);
        return fetchDataForMultipleSymbols(stockSymbols, "/cash-flow-statement/");
    }

    private List<String> fetchDataForMultipleSymbols(String stockSymbols, String endpoint) {
        List<String> data = new ArrayList<>();
        for (String symbol : stockSymbols.split(",")) {
            String response = fetchData(endpoint + symbol);
            data.add(response);
        }
        return data;
    }

    private String fetchData(String endpoint) {
        return restClient.get()
                .uri(endpoint + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(String.class)
                .replaceAll("\\s+", " ")
                .trim();
    }

    public StockPrice getPrice(String symbol) {
        log.info("getPrice.start | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());

        var priceList = restClient.get()
                .uri("/quote/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockPrice>>() {});

        if (priceList == null || priceList.isEmpty()) {
            log.warn("No stock price found for symbol: {}", symbol);
            throw new RuntimeException("No price found for the symbol: " + symbol);
        }

        log.info("getPrice.end | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());
        return priceList.get(0);
    }

    public CompanyDetails getCompanyDetails(String symbol) {
        log.info("getCompanyDetails.start | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());

        var companyList = restClient.get()
                .uri("/profile/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<CompanyDetails>>() {});

        if (companyList == null || companyList.isEmpty()) {
            log.warn("No company details found for symbol: {}", symbol);
            throw new RuntimeException("No company details found for the symbol: " + symbol);
        }

        log.info("getCompanyDetails.end | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());
        return companyList.get(0);
    }

    @Async
    public CompletableFuture<StockPrice> getPriceAsync(String symbol) {
        log.info("getPriceAsync.start | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());

        var priceList = restClient.get()
                .uri("/quote/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockPrice>>() {});

        if (priceList == null || priceList.isEmpty()) {
            log.warn("No stock price found for symbol: {}", symbol);
            throw new RuntimeException("No price found for the symbol: " + symbol);
        }

        log.info("getPriceAsync.end | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());
        return CompletableFuture.completedFuture(priceList.get(0));
    }

    @Async
    public CompletableFuture<CompanyDetails> getCompanyDetailsAsync(String symbol) {
        log.info("getCompanyDetailsAsync.start | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());

        var companyList = restClient.get()
                .uri("/profile/" + symbol + "?apikey=" + stockAPIConfig.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<List<CompanyDetails>>() {});

        if (companyList == null || companyList.isEmpty()) {
            log.warn("No company details found for symbol: {}", symbol);
            throw new RuntimeException("No company details found for the symbol: " + symbol);
        }

        log.info("getCompanyDetailsAsync.end | Symbol: {} | Thread: {}", symbol, Thread.currentThread().getName());
        return CompletableFuture.completedFuture(companyList.get(0));
    }
}