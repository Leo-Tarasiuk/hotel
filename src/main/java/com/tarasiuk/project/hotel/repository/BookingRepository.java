package com.tarasiuk.project.hotel.repository;

import com.tarasiuk.project.hotel.model.Booking;
import com.tarasiuk.project.hotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByRoom(Room room);

    @Query("select b from Booking b where b.active = 0")
    Page<Booking> findAllWaiting(Pageable pageable);

    @Modifying
    @Query("update Booking b set b.active = 1 where b.id = :id")
    void acceptBooking(@Param("id") Long booking_id);

    @Modifying
    @Query("delete from Booking b where b.id = :id")
    void deleteById(@Param("id") Long id);

    int countBookingsByActive(byte active);
}
