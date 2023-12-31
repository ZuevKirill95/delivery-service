package ru.sber.delivery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.delivery.entities.User;
import ru.sber.delivery.services.AdministrationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Класс отвечающий за обработку запросов администраторов
 */
@Slf4j
@RestController
@RequestMapping("administrators")
public class AdministratorController {
    private final AdministrationService administratorService;

    /**
     * Конструктор контроллера администраторов
     */
    @Autowired
    public AdministratorController(AdministrationService administratorService) {
        this.administratorService = administratorService;
    }

    /**
     * Возвращает информацию о курьере
     *
     * @param idUser - индификатор курьера
     * @return - данные о курьере
     */
    @GetMapping("/courier/{id}")
    public ResponseEntity<?> getCouriersData(@PathVariable("id") long idUser) {
        log.info("Получение информации о курьере");
        Optional<User> optionalUser = administratorService.findUser(idUser);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok().body(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Обновляет информацию о курьере
     *
     * @param user - новая информация о пользователе
     * @return - результат запроса
     */
    @PutMapping
    public ResponseEntity<?> updateCourier(@RequestBody User user) {
        log.info("Обновление данных о курьере");

        if (administratorService.update(user)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Удаляет курьера
     *
     * @param idUser - индификатор пользователя
     * @return - результат запроса
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable("id") long idUser) {
        log.info("Удаление курьера");
        User user = new User();
        user.setId(idUser);
        boolean isDeleted = administratorService.delete(user);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получает информацию о всех курьерах
     *
     * @return - список курьеров
     */
    @GetMapping("/all-couriers")
    public  ResponseEntity<List<User>> getAllCouriersData() {
        log.info("Получение информацию о курьерах");

        return ResponseEntity.ok().body(administratorService.findAllUsers());

    }

    /**
     * Получает информацию о всех курьерах вышедших на смену в заданный день
     *
     * @return - список курьеров
     */
    @GetMapping("/all-couriers/by-date")
    public ResponseEntity<List<User>> getAllShifts(@RequestParam("shiftDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shiftDate) {
        log.info("Получение информации о всех пользователях вышедших на смену в заданный день");
        System.out.println(shiftDate);
        return ResponseEntity.ok().body(administratorService.findUsersByShift(shiftDate));
    }


}
