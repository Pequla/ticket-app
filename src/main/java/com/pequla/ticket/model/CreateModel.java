package com.pequla.ticket.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateModel {
    private String email;
    private String password;
    private String name;
    private String country;
    private String city;
    private String address;
    private String phone;
}
