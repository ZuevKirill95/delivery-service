package ru.sber.delivery.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.delivery.entities.enum_model.EStatusCourier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Пользователь
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users"/*,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phoneNumber")
        }*/)
public class User {

    @Id
    @Column(name = "id_user")
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    // @NotBlank
    // @Size(max = 20)
    // private String username;
    // @NotBlank
    // @Size(max = 50)
    // @Email
    // private String email;
    // @NotBlank
    // @Size(max = 12)
    // private String phoneNumber;
    // @NotBlank
    // @Size(max = 120)
    // @JsonIgnore
    // private String password;

    // @Column(name = "date_registration")
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @Temporal(TemporalType.TIMESTAMP)
    // private LocalDateTime dateRegistration;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusCourier status;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;
    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(String id) {
        this.id = id;
    }

    public User(String id, String username, String email, String phoneNumber,
                    String password, LocalDateTime dateRegistration,
                    EStatusCourier status, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        // this.username = username;
        // this.email = email;
        // this.phoneNumber = phoneNumber;
        // this.password = password;
        // this.dateRegistration = dateRegistration;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
