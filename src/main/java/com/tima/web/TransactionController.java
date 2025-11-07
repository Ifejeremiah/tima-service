package com.tima.web;

import com.tima.dto.Response;
import com.tima.dto.TransactionCreateRequest;
import com.tima.dto.TransactionSummary;
import com.tima.dto.TransactionUpdateStatus;
import com.tima.model.Page;
import com.tima.model.Transaction;
import com.tima.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/transactions")
public class TransactionController {
    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Transaction> create(@RequestBody @Validated TransactionCreateRequest request) {
        transactionService.create(request);
        return new Response<>("Transaction created successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Transaction>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<Page<Transaction>> response = new Response<>();
        response.setData(transactionService.findAll(page, size, searchQuery));
        response.setMessage("Transactions fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Transaction> find(@PathVariable int id) {
        Response<Transaction> response = new Response<>();
        response.setData(transactionService.findById(id));
        response.setMessage("Transaction fetched successfully");
        return response;
    }

    @GetMapping(path = "reference/{transactionRef}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Transaction> findByTransactionReference(@PathVariable String transactionRef) {
        Response<Transaction> response = new Response<>();
        response.setData(transactionService.findByTransactionRef(transactionRef));
        response.setMessage("Transaction fetched by reference successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Transaction> updateStatus(@PathVariable int id, @RequestBody @Validated TransactionUpdateStatus request) {
        transactionService.update(id, request);
        return new Response<>("Transaction status updated successfully");
    }

    @GetMapping(path = "summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<TransactionSummary> findTransactionSummary() {
        Response<TransactionSummary> response = new Response<>();
        response.setData(transactionService.findTransactionSummary());
        response.setMessage("Transaction summary fetched successfully");
        return response;
    }
}
