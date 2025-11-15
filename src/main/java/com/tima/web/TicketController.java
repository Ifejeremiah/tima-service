package com.tima.web;

import com.tima.dto.Response;
import com.tima.dto.TicketCreateRequest;
import com.tima.dto.TicketSummary;
import com.tima.dto.TicketUpdateRequest;
import com.tima.model.Page;
import com.tima.model.Ticket;
import com.tima.model.TicketChat;
import com.tima.service.TicketChatService;
import com.tima.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/tickets")
public class TicketController {
    TicketService ticketService;
    TicketChatService ticketChatService;

    public TicketController(TicketService ticketService, TicketChatService ticketChatService) {
        this.ticketService = ticketService;
        this.ticketChatService = ticketChatService;
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

    @PostMapping(path = "/{ticketId}/chats", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<TicketChat> create(@PathVariable int ticketId, @RequestBody @Validated TicketChat request) {
        request.setTicketId(ticketId);
        ticketChatService.create(request);
        return new Response<>("Ticket chat created successfully");
    }

    @GetMapping(path = "/{ticketId}/chats", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<TicketChat>> findAll(
            @PathVariable int ticketId,
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size) {
        Response<Page<TicketChat>> response = new Response<>();
        response.setData(ticketChatService.findAll(page, size, ticketId));
        response.setMessage("Ticket chats fetched successfully");
        return response;
    }
}
