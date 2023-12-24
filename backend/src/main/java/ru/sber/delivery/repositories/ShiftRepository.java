package ru.sber.delivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.delivery.entities.Shift;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findAllByUserId(String userId);

    void deleteById(long id);

    default List<Shift> findShiftsByBeginShift(LocalDate beginShift) {
        return findShiftsByBeginShiftBetween(beginShift.atStartOfDay(), beginShift.plusDays(1).atStartOfDay());
    }
    List<Shift> findShiftsByBeginShiftBetween(LocalDateTime from, LocalDateTime to);
}
