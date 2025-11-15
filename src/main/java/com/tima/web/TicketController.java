package com.tima.web;

import com.tima.dto.Response;
import com.tima.dto.TicketCreateRequest;
import com.tima.dto.TicketSummary;
import com.tima.dto.TicketUpdateRequest;
import com.tima.model.Page;
import com.tima.model.Ticket;
import com.tima.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/tickets")
public class TicketController {
    TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Ticket> create(@RequestBody @Validated TicketCreateRequest request) {
        ticketService.create(request);
        return new Response<>("Ticket created successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Ticket>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "priority", required = false) String priority,
            @RequestParam(name = "category", required = false) String category) {
        Response<Page<Ticket>> response = new Response<>();
        response.setData(ticketService.findAll(page, size, searchQuery, status, priority, category));
        response.setMessage("Tickets fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Ticket> find(@PathVariable int id) {
        Response<Ticket> response = new Response<>();
        response.setData(ticketService.findById(id));
        response.setMessage("Ticket fetched successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Ticket> update(@PathVariable int id, @RequestBody @Validated TicketUpdateRequest request) {
        ticketService.update(id, request);
        return new Response<>("Ticket updated successfully");
    }

    @GetMapping(path = "summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<TicketSummary> findTicketSummary() {
        Response<TicketSummary> response = new Response<>();
        response.setData(ticketService.findTicketSummary());
        response.setMessage("Ticket summary fetched successfully");
        return response;
    }
}
