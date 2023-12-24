package ru.sber.delivery.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.delivery.entities.Notify;
import ru.sber.delivery.entities.Role;
import ru.sber.delivery.entities.User;
import ru.sber.delivery.entities.enum_model.ERole;
import ru.sber.delivery.entities.enum_model.EStatusCourier;
import ru.sber.delivery.repositories.RoleRepository;
import ru.sber.delivery.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работника ресторана
 */
@Slf4j
@Service
public class RestaurantEmployeeServiceImpl implements RestaurantEmployeeService {

    private final UserRepository userRepository;
    private final NotifyService notifyService;
    private final RoleRepository roleRepository;

    @Autowired
    public RestaurantEmployeeServiceImpl(UserRepository userRepository, AdministrationService administrationService, NotifyService notifyService, RoleRepository roleRepository) {
        this.userRepository = userRepository;

        this.notifyService = notifyService;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findFreeCouriers() {
        log.info("Поиск ближайших курьеров (со стороны работника ресторана)");
        return userRepository.findAllByStatus(EStatusCourier.FREE);
    }

    @Override
    public Optional<User> findNearestFreeCourier(BigDecimal restaurantLatitude, BigDecimal restaurantLongitude) {
        Optional<Role> role = roleRepository.findByRole(ERole.COURIER);
        log.info("Поиск ближайшего свободного курьера (со стороны работника ресторана)");
        if (role.isPresent()) {
            List<User> freeCouriers = userRepository.findUserByRoleAndStatus(role.get(), EStatusCourier.FREE);
            User nearestCourier = freeCouriers
                    .stream()
                    .min(Comparator.comparingDouble(user -> haversineDistance(user.getLongitude().doubleValue(), user.getLatitude().doubleValue(),
                            restaurantLongitude.doubleValue(), restaurantLatitude.doubleValue())))
                    .orElse(null);
            log.info("Ближайший курьер {}", nearestCourier);
            return Optional.ofNullable(nearestCourier);
        }
        log.warn("Курьер не  найден");
        return Optional.empty();
    }

    private double haversineDistance(double lon1, double lat1, double lon2, double lat2) {
        final double R = 6371.0;

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public boolean notifyCourier(String idUser, long idOrder) {
        log.info("Уведомление курьера о заказе");
        notifyService.save(new Notify(idUser, idOrder));
        return true;
    }

}
