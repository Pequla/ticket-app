package com.pequla.ticket.controller;

import com.pequla.ticket.entity.Ticket;
import com.pequla.ticket.model.TicketModel;
import com.pequla.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService service;

    @GetMapping
    public List<TicketModel> getAvailableTickets(@RequestParam String token) throws IOException {
        return service.getAvailableTickets(token);
    }

    @GetMapping(path = "/unused")
    public List<TicketModel> getUnused(@RequestParam String token) throws IOException {
        return service.getUnusedTickets(token);
    }

    @GetMapping(path = "/used")
    public List<TicketModel> getUsed(@RequestParam String token) throws IOException {
        return service.getUsedTickets(token);
    }

    @GetMapping(path = "/{id}")
    public TicketModel getById(@PathVariable Integer id, @RequestParam String token) throws IOException {
        return service.getTicketById(token, id);
    }

    @PostMapping
    public TicketModel save(@RequestBody Ticket ticket, @RequestParam String token) throws IOException {
        return service.saveTicket(token, ticket);
    }

    @PutMapping(path = "/{id}")
    public TicketModel setRating(@PathVariable Integer id, @RequestParam String token, @RequestParam Double rating) throws IOException {
        return service.setRating(token, id, rating);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id, @RequestParam String token) throws IOException {
        service.deleteTicket(token, id);
    }
}
