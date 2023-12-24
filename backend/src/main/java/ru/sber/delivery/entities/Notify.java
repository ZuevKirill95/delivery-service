package ru.sber.delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notify {
    @Id
    @Column(name = "id_notify")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_id", nullable = false)
    private long orderId;

    public Notify(String userId, long orderId) {
        User userModel = new User();
        userModel.setId(userId);
        this.user = userModel;
        this.orderId = orderId;
    }
}
