package ru.sber.delivery.services;

import ru.sber.delivery.entities.User;
import ru.sber.delivery.entities.enum_model.EStatusCourier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Отвечает за работу курьера
 */
public interface CourierService {


    boolean addUserById(String userId);
    /**
     * Возвращает информацию о курьере
     *
     * @return информация о курьере
     */
    Optional<User> findUser();

    /**
     * Обновляет информацию о курьере
     *
     * @param user - новые данные курьера
     * @return true в случае успеха
     */
    boolean update(User user);

    /**
     * Обновляет статус курьера
     *
     * @param statusCourier - новый статус
     * @return true в случае успеха
     */
    boolean updateUserStatus(EStatusCourier statusCourier);

    /**
     * Обновляет местоположение курьера
     *
     * @param latitude  - широта
     * @param longitude - долгота
     * @return true в случае успеха
     */
    boolean updateUserCoordinates(BigDecimal latitude, BigDecimal longitude);

    List<?> notifyCourier();
}
