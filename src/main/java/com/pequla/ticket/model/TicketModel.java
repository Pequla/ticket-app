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
    private String airline;
    private Integer count;
    private Boolean oneWay;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
}
