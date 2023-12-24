package ru.sber.delivery.services;

import java.time.LocalDateTime;

import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtService {

    String getSubClaim(Jwt jwt);
    String getEmailClaim(Jwt jwt);
    String getPhoneNumberClaim(Jwt jwt);
    String getPreferredUsernameClaim(Jwt jwt);
    Long getCreatedTimestampClaim(Jwt jwt);
}
