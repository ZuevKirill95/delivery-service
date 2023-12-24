package ru.sber.delivery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.delivery.entities.Shift;
import ru.sber.delivery.services.ShiftService;

import java.net.URI;
import java.util.List;

/**
 * Класс отвечающий за обработку запросов о сменах
 */
@Slf4j
@RestController
@RequestMapping("shifts")
public class ShiftController {
    private final ShiftService shiftService;

    /**
     * Конструктор контроллера смен
     */
    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    /**
     * Добавляет смену
     *
     * @return - результат запроса
     */
    @PostMapping
    public ResponseEntity<?> addShift() {
        log.info("Добавление смены");
        long id = shiftService.save();

        if (id != 0) {
            return ResponseEntity.created(URI.create("/shifts/" + id)).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Обновляет информацию о смене
     *
     * @param shift - новая информация о смене
     * @return - результат запроса
     */
    @PutMapping
    public ResponseEntity<?> updateShift(@RequestBody Shift shift) {
        log.info("Обновление данных о смене");

        if (shiftService.update(shift)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Удаляет смену
     *
     * @param idShift - индификатор смены
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShift(@PathVariable("id") long idShift) {
        log.info("Удаление смены");
        boolean isDeleted = shiftService.delete(idShift);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получает информацию о всех сменах курьера
     *
     * @param idUser - индификатор пользователя
     * @return - смены пользователя
     */
    @GetMapping("/courier/{id}")
    public ResponseEntity<List<Shift>> getAllCouriersShifts(@PathVariable("id") String idUser) {
        log.info("Получение информации о всех сменах курьера");
        return ResponseEntity.ok().body(shiftService.findAllShiftsByUser(idUser));

    }


}
