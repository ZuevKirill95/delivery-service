package ru.sber.delivery.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sber.delivery.entities.User;
import ru.sber.delivery.models.Order;
import ru.sber.delivery.services.RestaurantEmployeeService;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class KafkaListeners {
    private final RestaurantEmployeeService restaurantEmployeeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KafkaListeners(RestaurantEmployeeService restaurantEmployeeService) {
        this.restaurantEmployeeService = restaurantEmployeeService;
    }

    @KafkaListener(topics = "courier_status", groupId = "newGroup")
    void listenerNotify(ConsumerRecord<String, Object> record) throws IOException {
        log.info("Обновляет id курьера у заказа с id {}", record);
        objectMapper.registerModule(new JavaTimeModule());
        String value = record.value().toString();
        Order order = objectMapper.readValue(value, Order.class);
        log.info("Order: {}", order);
        String apiKey = "8ec18778-cb70-437f-87fc-7c17e8e0bb71";
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://geocode-maps.yandex.ru/1.x/?apikey=" + apiKey + "&geocode=" + order.getBranchAddress() + "&format=json";
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        log.info("Определение по адресу: {}", rootNode);
        JsonNode posNode = rootNode.path("response").path("GeoObjectCollection").path("featureMember").get(0).path("GeoObject").path("Point").path("pos");
        String pos = posNode.asText();
        log.info("Ответ: {}.", pos);
        String[] numbers = pos.split(" ");
        double pos1 = Double.parseDouble(numbers[0]);
        double pos2 = Double.parseDouble(numbers[1]);

        Optional<User> user = restaurantEmployeeService.findNearestFreeCourier(BigDecimal.valueOf(pos1), BigDecimal.valueOf(pos2));

        user.ifPresent(user1 -> restaurantEmployeeService.notifyCourier(user1.getId(), order.getId()));
    }
}
