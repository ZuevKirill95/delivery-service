package ru.sber.delivery.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sber.delivery.entities.Notify;
import ru.sber.delivery.exceptions.UserNotFound;
import ru.sber.delivery.repositories.NotifyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
// import ru.sber.delivery.security.services.UserDetailsImpl;

import java.util.List;

@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    private final NotifyRepository notifyRepository;
    private final JwtService jwtService;

    @Autowired
    public NotifyServiceImpl(NotifyRepository notifyRepository, JwtService jwtService) {
        this.notifyRepository = notifyRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<Notify> findNotifyByUserId() {
        return notifyRepository.findAllByUser_Id(getUserIdSecurityContext());
    }

    @Override
    public boolean save(Notify notify) {
        log.info("Создание уведомления {}", notify);
        notifyRepository.save(notify);
        return true;
    }

    @Override
    public boolean update(Notify notify) {
        log.info("Обновление уведомления {}", notify);
        if(notifyRepository.existsById(notify.getId())){
            notifyRepository.save(notify);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Notify notify) {
        log.info("Удаление уведомления {}", notify);
        if(notifyRepository.existsById(notify.getId())){
            notifyRepository.delete(notify);
            return true;
        }
        return false;
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
