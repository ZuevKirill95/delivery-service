package ru.sber.delivery.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class TokenResponse {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private long not_before_policy;
    private String session_state;
    private String scope;
}