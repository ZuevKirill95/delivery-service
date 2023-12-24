package ru.sber.delivery.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sber.delivery.entities.Shift;
import ru.sber.delivery.entities.User;
import ru.sber.delivery.exceptions.UserNotFound;
import ru.sber.delivery.repositories.ShiftRepository;
// import ru.sber.delivery.security.services.UserDetailsImpl;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса смены
 */
@Slf4j
@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final JwtService jwtService;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository, JwtService jwtService) {
        this.shiftRepository = shiftRepository;
        this.jwtService = jwtService;
    }

    @Override
    public long save() {
        log.info("Смена создана");
        Shift shift = new Shift();
        shift.setBeginShift(LocalDateTime.now());
        User user = new User();
        user.setId(getUserIdSecurityContext());
        shift.setUser(user);
        return shiftRepository.save(shift).getId();
    }

    @Override
    public boolean update(Shift shift) {
        log.info("Обновление смены");
        User user = new User();
        user.setId(getUserIdSecurityContext());
        if (shiftRepository.existsById(shift.getId())) {
            log.info("Обновление успешно");
            shift.setUser(user);
            shiftRepository.save(shift);
            return true;
        }
        log.warn("Обновление ровалено");
        return false;
    }

    @Override
    public boolean delete(long idShift) {
        log.info("Удапление смены");
        if (shiftRepository.existsById(idShift)) {
            log.info("Удапление успешно");
            shiftRepository.deleteById(idShift);
            return true;
        }
        log.warn("Удаление провалено");
        return false;
    }

    @Override
    public List<Shift> findAllShiftsByUser(String idUser) {
        log.info("Поиск смен пользователя");
        return shiftRepository.findAllByUserId(idUser);
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

}
