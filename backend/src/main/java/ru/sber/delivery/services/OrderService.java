package ru.sber.delivery.services;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Получает информацию об order
 */
public interface OrderService {
    /**
     * Обновляет статус заказа
     *@param id ндификатор заказа
     * @param order заказ
     * @return ответ
     */
    ResponseEntity<?> updateOrderStatus(Long id, Object order);
    /**
     * Устанавливает курьера на заказ
     * @param order заказ
     * @return ответ
     */
    ResponseEntity<?> updateOrderCourierId(Object order);

    /**
     * Ищет список заказов которые готовятся или уже готовы, но не доставляются, ограниченный страницей
     *
     * @return список заказов
     */
    Page<?> findAllActiveOrdersByPage(int page, int pageSize);

    /**
     * Ищет заказ по id
     *
     * @return заказ
     */
    Optional<?> findOrderById(long idOrder);
    /**
     * Возвращает все заказы которые брал курьер
     *
     * @return список заказов курьера
     */
    Page<?> findOrdersByCourierId(int page, int pageSize);

    List<?> getOrdersIsDelivering();
    Integer getCountOrderCourier();
}
