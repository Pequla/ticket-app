package com.pequla.ticket.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private String name;
    private String message;
    private String path;
    private Long timestamp;
}
