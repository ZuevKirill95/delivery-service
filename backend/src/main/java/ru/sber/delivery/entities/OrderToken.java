package ru.sber.delivery.entities;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderToken {
    @Id
    private Integer id;

    @Column(name = "access_token", length = 10000)
    private String accessToken;

    @Column(name = "token_expiration")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tokenExpiration;
}
