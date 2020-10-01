package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Booking;
import com.tarasiuk.project.hotel.model.Room;
import com.tarasiuk.project.hotel.repository.BookingRepository;
import com.tarasiuk.project.hotel.repository.RoomRepository;
import com.tarasiuk.project.hotel.service.BookingService;
import com.tarasiuk.project.hotel.service.exception.BookingException;
import com.tarasiuk.project.hotel.service.exception.DeleteException;
import com.tarasiuk.project.hotel.service.exception.ErrorCodes;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookingServiceImpl implements BookingService {

    private static final byte MAX_BOOKINGS = 8;
    private static final byte MIN_DAYS = 1;
    private static final byte EVEN_MONTH = 30;
    private static final byte ODD_MONTH = 31;
    private static final byte FEBRUARY = 28;
    private static final byte LEAP_FEBRUARY = 29;
    private static final byte ACTIVE = 0;

    private final CustomLocaleResolver customLocaleResolver;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final EmailService emailService;

    public BookingServiceImpl(BookingRepository bookingRepository, CustomLocaleResolver customLocaleResolver,
                              RoomRepository roomRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.customLocaleResolver = customLocaleResolver;
        this.roomRepository = roomRepository;
        this.emailService = emailService;
    }

    @Override
    public Booking save(Account account, String firstDay, String lastDay, String comfort, String placement) {
        Room room = roomRepository.findByComfortNameAndPlacement(comfort, Byte.parseByte(placement));
        Booking booking = Booking.builder()
                .account(account)
                .room(room)
                .firstDay(convertToLocalDate(firstDay))
                .lastDay(convertToLocalDate(lastDay))
                .build();
        booking.setCost(calcCost(room.getPrice(), booking.getFirstDay(), booking.getLastDay()));
        checkBooking(booking);
        return bookingRepository.save(booking);
    }

    @Override
    public void delete(String id) {
        Optional<Booking> findBooking = bookingRepository.findById(Long.parseLong(id));
        if (!findBooking.isPresent()) {
            throw new DeleteException(customLocaleResolver.messageSource()
                    .getMessage("errorMessage.DeleteException", null,
                            LocaleContextHolder.getLocale()), ErrorCodes.DELETE_EXCEPTION.getErrorCode());
        }
        bookingRepository.deleteById(findBooking.get().getId());
        emailService.sendCancelBooking(findBooking.get());
    }

    @Override
    public List<Booking> getAll(Pageable pageable) {
        return bookingRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Booking> getAllWaiting(Pageable pageable) {
        return bookingRepository.findAllWaiting(pageable).getContent();
    }

    @Override
    public boolean acceptBooking(Long booking_id) {
        bookingRepository.acceptBooking(booking_id);
        Booking booking = bookingRepository.getOne(booking_id);
        emailService.sendAcceptBooking(booking);
        return true;
    }

    @Override
    public int count(int size) {
        return (int) (bookingRepository.count() / size);
    }

    @Override
    public int countWaiting(int size) {
        return (int)(bookingRepository.countBookingsByActive(ACTIVE) / size);
    }

    private void checkBooking(Booking booking) {
        List<Booking> bookings = bookingRepository.findBookingByRoom(booking.getRoom());
        byte count = 0;

        for (Booking iterator : bookings) {
            if (compareDate(iterator, booking.getFirstDay(), booking.getLastDay())) {
                count++;
            }
        }

        if (count == MAX_BOOKINGS) {
            throw new BookingException(customLocaleResolver.messageSource()
                    .getMessage("errorMessage.BookingException", null,
                            LocaleContextHolder.getLocale()), ErrorCodes.SAVE_EXCEPTION.getErrorCode());
        }
    }

    private LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return LocalDate.parse(date, formatter);
    }

    private boolean compareDate(Booking booking, LocalDate first, LocalDate last) {
        return last.compareTo(booking.getFirstDay()) <= 0 || first.compareTo(booking.getLastDay()) >= 0;
    }

    private int calcCost(short price, LocalDate first, LocalDate last) {
        int days = MIN_DAYS;
        if (first.getMonthValue() != last.getMonthValue()) {
            if (first.getMonthValue() % 2 == 0) {
                if (first.getMonthValue() == 2) {
                    if (isLeapYear(first.getYear())) {
                        days += LEAP_FEBRUARY - first.getDayOfMonth() + last.getDayOfMonth();
                    } else {
                        days += FEBRUARY - first.getDayOfMonth() + last.getDayOfMonth();
                    }
                } else {
                    days += EVEN_MONTH - first.getDayOfMonth() + last.getDayOfMonth();
                }
            } else {
                days += ODD_MONTH - first.getDayOfMonth() + last.getDayOfMonth();
            }
        } else {
            days += last.getDayOfMonth() - first.getDayOfMonth();
        }

        return days * price;
    }

    private boolean isLeapYear(int year) {
        return (year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0));
    }
}
