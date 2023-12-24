package ru.sber.delivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.sber.delivery.entities.OrderToken;

@Repository
public interface OrderTokenRepository extends JpaRepository<OrderToken, Integer> {
}
