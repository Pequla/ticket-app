package com.pequla.ticket.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenModel {
    private UserModel user;
    private LocalDateTime issuedAt;
}
