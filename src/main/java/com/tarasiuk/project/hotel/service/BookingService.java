package com.tarasiuk.project.hotel.service;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Booking;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    Booking save(Account account, String firstDay, String lastDay, String comfort, String placement);

    void delete(String id);

    List<Booking> getAll(Pageable pageable);

    List<Booking> getAllWaiting(Pageable pageable);

    boolean acceptBooking(Long booking_id);

    int count(int size);

    int countWaiting(int size);
}
