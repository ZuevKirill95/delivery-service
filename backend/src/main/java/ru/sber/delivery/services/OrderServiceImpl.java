package ru.sber.delivery.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.sber.delivery.entities.OrderToken;
import ru.sber.delivery.exceptions.UserNotFound;
import ru.sber.delivery.order.OrderFeign;
// import ru.sber.delivery.security.services.UserDetailsImpl;

/**
 * Реализует логику работы с order-service
 */

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderFeign orderFeign;
    private final JwtService jwtService;
    private final OrderTokenService orderTokenService;

    public OrderServiceImpl(OrderFeign orderFeign, JwtService jwtService, OrderTokenService orderTokenService) {
        this.orderFeign = orderFeign;
        this.jwtService = jwtService;
        this.orderTokenService = orderTokenService;

    }

    public ResponseEntity<?> updateOrderStatus(Long id, Object order) {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return orderFeign.updateOrderStatus("Bearer "+orderToken.get(0).getAccessToken(), id, order);
    }

    public ResponseEntity<?> updateOrderCourierId(Object order) {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return orderFeign.updateOrderCourier("Bearer "+orderToken.get(0).getAccessToken(), getUserIdSecurityContext(), order);
    }

    @Override
    public Page<?> findAllActiveOrdersByPage(int page, int pageSize) {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return orderFeign.findAllActiveOrdersByPage("Bearer "+orderToken.get(0).getAccessToken(), page, pageSize).getBody();
    }

    public Optional<?> findOrderById(long idOrder) {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return Optional.of(Objects.requireNonNull(orderFeign.getOrderById("Bearer "+orderToken.get(0).getAccessToken(), idOrder).getBody()));
    }

    @Override
    public Page<?> findOrdersByCourierId(int page, int pageSize) {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return orderFeign.getAllOrdersByCourierId("Bearer "+orderToken.get(0).getAccessToken(), getUserIdSecurityContext(), page, pageSize).getBody();
    }

    @Override
    public List<?> getOrdersIsDelivering() {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return orderFeign.getOrdersIsDelivering("Bearer "+orderToken.get(0).getAccessToken(), getUserIdSecurityContext()).getBody();
    }

    @Override
    public Integer getCountOrderCourier() {
        checkAndUpdateOrderTokens();
        List<OrderToken> orderToken = orderTokenService.findAll();
        return orderFeign.getCountOrderFromCourier("Bearer "+orderToken.get(0).getAccessToken(), getUserIdSecurityContext()).getBody();
    }

    /**
     * Возвращает id user из security context
     *
     * @return идентификатор пользователя
     */
    private String getUserIdSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuthenticationToken.getToken();
            String subClaim = jwtService.getSubClaim(jwt);

            return subClaim;
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }

    private void checkAndUpdateOrderTokens() {
        List<OrderToken> orderToken = orderTokenService.findAll();
    
        if (orderToken.isEmpty()) {
            log.info("Токен заказа отсутствует, отправка запроса на получение токена");
            updateOrderTokens();
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
    
            if (orderToken.stream().allMatch(token -> currentDateTime.isBefore(token.getTokenExpiration()))) {
                log.info("Список токенов не пуст и токен действителен. Нет необходимости обновлять токен заказа");
            } else {
                log.info("Список токенов содержит токен с истекшим сроком действия. Отправка запроса на получение токена");
                updateOrderTokens();
            }
        }
    }

    @Transactional
    private void updateOrderTokens() {
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "password");
        tokenBody.add("client_id", "login-app");
        tokenBody.add("username", "user");
        tokenBody.add("password", "password");

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        try {
                ResponseEntity<String> tokenResponseEntity = new RestTemplate().exchange(
                        "http://localhost:8080/realms/order-realm/protocol/openid-connect/token", 
                        HttpMethod.POST, tokenEntity, String.class);

                String jsonResponse = tokenResponseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                
                String accessToken = jsonNode.get("access_token").asText();

                LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(14);

                OrderToken newToken = new OrderToken((Integer) 1, accessToken, expirationTime);
                
                orderTokenService.save(newToken);
                log.info("Токен заказа был обновлён успешно");
        } catch (Exception e) {
                e.printStackTrace();
        }

    }
    
}
