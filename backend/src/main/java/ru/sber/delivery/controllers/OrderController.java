package ru.sber.delivery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.delivery.models.Order;
import ru.sber.delivery.models.OrderStatus;
import ru.sber.delivery.services.OrderService;

import java.util.List;

/**
 * Получает Rest запросы к сервису заказов
 */
@Slf4j
@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    /**
     * Возвращает заказы ожидающие доставки, ограниченные страницей
     *
     * @return список заказов
     */
    @GetMapping("/awaiting-delivery")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<Page<?>> getActiveOrder(@RequestParam int page) {
        int pageSize = 10;
        return ResponseEntity.ok(orderService.findAllActiveOrdersByPage(page, pageSize));
    }

    /**
     * Возвращает заказы которые доставляет курьер 
     *
     * @return заказы курьера
     */
    @GetMapping("/delivering")
    @PreAuthorize("hasRole('client_user')")
    ResponseEntity<List<?>> getDeliveringOrdersByCourierId() {
        return ResponseEntity.ok(orderService.getOrdersIsDelivering());
    }

    /**
     * Устанавливает курьера на заказ
     *
     * @param order содержит id курьера и id заказа
     */
    @PutMapping("/courier")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> updateOrderCourier(@RequestBody Object order) {
        log.info("Установление курьера на заказ {}", order);
        orderService.updateOrderCourierId(order);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет статус заказа
     *
     */
    @PutMapping("/{idOrder}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("idOrder") Long id, @RequestBody OrderStatus orderStatus) {
        log.info("обновление статуса заказа с id{}", id);
        return orderService.updateOrderStatus(id, orderStatus);
    }

    /**
     * Возвращает заказ по id
     *
     * @return заказ
     */
    @GetMapping("/{idOrder}")
    @PreAuthorize("hasRole('client_user')")
    ResponseEntity<?> getOrderById(@PathVariable long idOrder) {
        return ResponseEntity.ok(orderService.findOrderById(idOrder));
    }

    /**
     * Возвращает все заказы курьера
     *
     * @return заказы курьера
     */
    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    ResponseEntity<Page<?>> getAllOrdersByCourierId(@RequestParam int page) {
        int pageSize = 10;
        return ResponseEntity.ok(orderService.findOrdersByCourierId(page, pageSize));
    }

    @GetMapping("/analytic/count")
    ResponseEntity<Integer> getAllOrdersByCourierId() {
        log.info("Возвращает количество заказов совершил курьер");
        return ResponseEntity.ok(orderService.getCountOrderCourier());
    }
}
