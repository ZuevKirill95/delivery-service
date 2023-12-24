package ru.sber.delivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.delivery.entities.Notify;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    List<Notify> findAllByUser_Id(String id);
}
