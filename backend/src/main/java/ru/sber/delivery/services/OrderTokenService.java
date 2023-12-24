package ru.sber.delivery.services;

import java.util.List;

import ru.sber.delivery.entities.OrderToken;

public interface OrderTokenService {
    boolean save(OrderToken orderToken);
    List<OrderToken> findAll();
}
