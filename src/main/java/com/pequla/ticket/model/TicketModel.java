package com.pequla.ticket.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketModel {
    private Integer id;
    private FlightModel flight;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
}
