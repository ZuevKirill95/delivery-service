package ru.sber.delivery.services;

import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class JwtServiceImpl implements JwtService{

    @Override
    public String getSubClaim(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    @Override
    public String getEmailClaim(Jwt jwt) {
        return jwt.getClaim("email");
    }

    @Override
    public String getPhoneNumberClaim(Jwt jwt) {
        return jwt.getClaim("phone_number");
    }

    @Override
    public String getPreferredUsernameClaim(Jwt jwt) {
        return jwt.getClaim("preferred_username");
    }
}