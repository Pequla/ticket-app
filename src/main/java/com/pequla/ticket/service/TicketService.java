package com.pequla.ticket.service;

import com.pequla.ticket.entity.AppUser;
import com.pequla.ticket.entity.Ticket;
import com.pequla.ticket.error.NotFoundException;
import com.pequla.ticket.model.FlightModel;
import com.pequla.ticket.model.TicketModel;
import com.pequla.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepo;
    private final MailService mailService;
    private final UserService userService;
    private final WebService webService;

    public List<TicketModel> getAvailableTickets(String token) throws IOException {
        HashMap<Integer, FlightModel> flightCachedResults = new HashMap<>();
        AppUser user = userService.getUserFromToken(token);
        return ticketRepo.findAllByUserAndUsedAtIsNull(user)
                .stream()
                .filter(ticket -> {
                    FlightModel flight = webService.getFlightById(ticket.getFlightId());
                    if (flight.getScheduledAt().isAfter(LocalDateTime.now())) {
                        flightCachedResults.put(ticket.getId(), flight);
                        return true;
                    }
                    return false;
                })
                .map(ticket -> makeModel(ticket, flightCachedResults.get(ticket.getId()))).collect(Collectors.toList());
    }

    public List<TicketModel> getUnusedTickets(String token) throws IOException {
        AppUser user = userService.getUserFromToken(token);
        return ticketRepo.findAllByUserAndUsedAtIsNull(user)
                .stream().map(this::makeModel).collect(Collectors.toList());
    }

    public List<TicketModel> getUsedTickets(String token) throws IOException {
        AppUser user = userService.getUserFromToken(token);
        return ticketRepo.findAllByUserAndUsedAtIsNotNull(user)
                .stream().map(this::makeModel).collect(Collectors.toList());
    }

    public TicketModel getTicketById(String token, Integer id) throws IOException {
        AppUser user = userService.getUserFromToken(token);
        Optional<Ticket> optional = ticketRepo.findByIdAndUser(id, user);
        if (optional.isEmpty()) throw new NotFoundException();
        return makeModel(optional.get());
    }

    public TicketModel saveTicket(String token, Ticket ticket) throws IOException {
        AppUser user = userService.getUserFromToken(token);
        ticket.setUser(user);
        ticket.setCreatedAt(LocalDateTime.now());

        TicketModel model = makeModel(ticketRepo.save(ticket));
        mailService.send(user.getEmail(),
                "You have successfully booked a ticket " + model.getId() + " for flight " + model.getFlight().getFlightKey(),
                "Ticket information");
        return model;
    }

    public void deleteTicket(String token, Integer id) throws IOException {
        AppUser user = userService.getUserFromToken(token);
        Optional<Ticket> optional = ticketRepo.findByIdAndUser(id, user);
        if (optional.isEmpty()) throw new NotFoundException();
        ticketRepo.delete(optional.get());
        mailService.send(user.getEmail(),
                "You have successfully deleted your ticket " + id,
                "Ticket deleted");
    }

    public TicketModel setRating(String token, Integer id, Double rating) throws IOException {
        AppUser user = userService.getUserFromToken(token);
        Optional<Ticket> optional = ticketRepo.findByIdAndUser(id, user);
        if (optional.isEmpty()) throw new NotFoundException();
        Ticket ticket = optional.get();

        if (ticket.getUsedAt() == null) {
            throw new RuntimeException("Ticket has not been used yet");
        }

        ticket.setRating(rating);
        return makeModel(ticket);
    }

    private TicketModel makeModel(Ticket ticket) {
        FlightModel flight = webService.getFlightById(ticket.getFlightId());
        return makeModel(ticket, flight);
    }

    private TicketModel makeModel(Ticket ticket, FlightModel flight) {
        return TicketModel.builder()
                .id(ticket.getId())
                .flight(flight)
                .airline(ticket.getAirline())
                .count(ticket.getCount())
                .oneWay(ticket.getOneWay())
                .createdAt(ticket.getCreatedAt())
                .usedAt(ticket.getUsedAt())
                .rating(ticket.getRating())
                .build();
    }
}
