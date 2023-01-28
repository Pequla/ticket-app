package com.pequla.ticket.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordModel {
    private String oldPassword;
    private String newPassword;
}
