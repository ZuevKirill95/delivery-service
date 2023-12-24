package ru.sber.delivery.services;

import ru.sber.delivery.entities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Отвечает за работу работника ресторана
 */
public interface RestaurantEmployeeService {

    /**
     * Возвращает свободных курьеров
     * @return список курьеров
     */
    List<User> findFreeCouriers();

    /**
     * Возвращает ближайшего свободного курьера
     *
     * @return ближайший курьер
     */
    Optional<User> findNearestFreeCourier(BigDecimal restaurantLatitude, BigDecimal restaurantLongitude);

    /**
     * Уведомляет курьера о заказе
     * @param idUser - номер пользователя которого требуется уведомить
     * @return true в случае успеха
     */
    boolean notifyCourier(String idUser, long idOrder);

}
