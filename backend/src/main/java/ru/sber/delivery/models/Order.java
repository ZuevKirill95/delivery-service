package ru.sber.delivery.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

/**
 * Заказ
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private String clientId;
    private String clientName;
    private String description;
    private String clientPhone;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime orderCookingTime;
    private LocalDateTime orderCookedTime;
    private String address;
    private String branchAddress;
    private Long branchId;
    private Long employeeRestaurantId;
    private List<?> dishesOrders;

}