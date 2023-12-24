package ru.sber.delivery.controllers;

import jakarta.transaction.Transactional;
import ru.sber.delivery.exceptions.UserNotFound;
import ru.sber.delivery.models.Attributes;
import ru.sber.delivery.models.Credential;
import ru.sber.delivery.models.LoginRequest;
import ru.sber.delivery.models.RefreshToken;
import ru.sber.delivery.models.SignupRequest;
import ru.sber.delivery.models.UserRequest;
import ru.sber.delivery.models.UserDetails;
import ru.sber.delivery.services.CourierService;
import ru.sber.delivery.services.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.*;


import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final String keycloakTokenUrl = "http://localhost:8080/realms/delivery-realm/protocol/openid-connect/token";
    private final String keycloakCreateUserUrl = "http://localhost:8080/admin/realms/delivery-realm/users";
    private final String clientId = "login-app";
    private final String grantType = "password";
    
    private final CourierService courierService;
    private final JwtService jwtService;
    
    @Autowired
    public AuthController(CourierService courierService, JwtService jwtService) {
        this.courierService = courierService;
        this.jwtService = jwtService;
    }

    
    @Transactional
    @PostMapping("/signup")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<String> signUpUser(@RequestBody SignupRequest signupRequest) {
        Jwt jwtToken = getUserJwtTokenSecurityContext(); 

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signupRequest.getUsername());
        userRequest.setEmail(signupRequest.getEmail());
        userRequest.setEnabled(true);

        Attributes attributes = new Attributes();
        attributes.setPhoneNumber(signupRequest.getPhoneNumber());
        userRequest.setAttributes(attributes);

        Credential credential = new Credential();
        credential.setType(grantType);
        credential.setValue(signupRequest.getPassword());

        List<Credential> credentials = new ArrayList<>();
        credentials.add(credential);
        userRequest.setCredentials(credentials);

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);
        userHeaders.setBearerAuth(jwtToken.getTokenValue());

        HttpEntity<UserRequest> userEntity = new HttpEntity<>(userRequest, userHeaders);

        try {
                ResponseEntity<String> userResponseEntity = new RestTemplate().exchange(
                        keycloakCreateUserUrl, HttpMethod.POST, userEntity, String.class);
                
                String responseHeader = userResponseEntity.getHeaders().get("Location").get(0);

                int lastSlashIndex = responseHeader.lastIndexOf("/");
                String userId = responseHeader.substring(lastSlashIndex + 1);

                courierService.addUserById(userId);

                return new ResponseEntity<>(userResponseEntity.getStatusCode());
        } catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signInUser(@RequestBody LoginRequest loginRequest) {
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", grantType);
        tokenBody.add("client_id", clientId);
        tokenBody.add("username", loginRequest.getUsername());
        tokenBody.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        try {
                ResponseEntity<String> tokenResponseEntity = new RestTemplate().exchange(
                        keycloakTokenUrl, HttpMethod.POST, tokenEntity, String.class);

                return new ResponseEntity<>(tokenResponseEntity.getBody(), 
                        tokenResponseEntity.getStatusCode());
        } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshUser(@RequestBody RefreshToken refreshToken) {
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "refresh_token");
        tokenBody.add("client_id", clientId);
        tokenBody.add("refresh_token", refreshToken.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        try {
                ResponseEntity<String> tokenResponseEntity = new RestTemplate().exchange(
                        keycloakTokenUrl, HttpMethod.POST, tokenEntity, String.class);

                return new ResponseEntity<>(tokenResponseEntity.getBody(), 
                        tokenResponseEntity.getStatusCode());
        } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<UserDetails> getUserDetails() {
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);

        Jwt jwt = getUserJwtTokenSecurityContext();
        UserDetails userDetails = new UserDetails(jwtService.getSubClaim(jwt), jwtService.getPreferredUsernameClaim(jwt), 
                jwtService.getEmailClaim(jwt), jwtService.getPhoneNumberClaim(jwt), jwtService.getCreatedTimestampClaim(jwt));

        return new ResponseEntity<>(userDetails, userHeaders, HttpStatus.OK);
    }

    
    private Jwt getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuthenticationToken.getToken();

            return jwt;
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }
}