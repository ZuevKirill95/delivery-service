package ru.sber.delivery.services;

import ru.sber.delivery.entities.Notify;

import java.util.List;

public interface NotifyService {
    List<Notify> findNotifyByUserId();
    boolean save(Notify notify);
    boolean update(Notify notify);
    boolean delete(Notify notify);
}
