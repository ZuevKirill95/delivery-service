package ru.sber.delivery.services;

import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.delivery.entities.Shift;
import ru.sber.delivery.entities.User;
import ru.sber.delivery.entities.enum_model.ERole;
// import ru.sber.delivery.repositories.ShiftRepository;
import ru.sber.delivery.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для администратора
 */
@Slf4j
@Service
public class AdministrationServiceImpl implements AdministrationService {

    private final UserRepository userRepository;

    @Autowired
    public AdministrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean update(User user) {
        log.info("Обновление данных пользователя: {}", user.getId());
        if (userRepository.existsById(user.getId())) {
            log.info("Обновление пользователя: {}, успешно", user.getId());
            userRepository.save(user);
            return true;
        }
        log.warn("Обновление пользователя: {}, провалено!", user.getId());
        return false;
    }

    @Override
    public boolean delete(User user) {
        log.info("Удаление данных пользователя: {}", user.getId());
        if (userRepository.existsById(user.getId())) {
            log.info("Удаление пользователя успешно");
            userRepository.delete(user);
            return true;
        }
        log.warn("Удаление пользователя провалено");
        return false;
    }

    @Override
    public Optional<User> findUser(String idUser) {
        log.info("Поиск пользователя со стороны администратора: {}", idUser);
        return userRepository.findById(idUser);
    }

    @Override
    public List<User> findAllUsers() {
        log.info("Поиск пользователей со стороны администратора");
        return userRepository.findAll()
                .stream()
                .toList();
    }


}
