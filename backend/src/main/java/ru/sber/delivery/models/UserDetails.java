package ru.sber.delivery.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDetails {
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private Long registrationDate;
}
