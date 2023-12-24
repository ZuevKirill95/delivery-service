package ru.sber.delivery.services;

import ru.sber.delivery.entities.Shift;
import ru.sber.delivery.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Отвечает за работу администратора
 */
public interface AdministrationService {

    /**
     * Обновляет данные пользователя
     * @param user новые данные администратора
     * @return true - в случае успеха
     */
    boolean update(User user);

    /**
     * Удаляет данные о пользователе
     * @param user - пользователь
     * @return true - в случае успеха
     */
    boolean delete(User user);

    /**
     * Возвращает информацию о пользователе по id
     * @param idUser - id пользователя
     * @return данные о пользователе
     */
    Optional<User> findUser(String idUser);

    /**
     * Возвращает информацию о всех пользователях, кроме администрации
     * @return список пользователей
     */
    List<User> findAllUsers();

    /**
     * Возвращает пользоватлей вышедших на смену в заданный день
     * @param dateShift - день смены
     * @return - Список пользователей
     */
    List<User> findUsersByShift(LocalDate dateShift);
}
