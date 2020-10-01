package com.tarasiuk.project.hotel.controller;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Booking;
import com.tarasiuk.project.hotel.service.BookingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes(names = {"account", "page"})
@RequestMapping("/")
public class BookingController {

    private static final int DEFAULT_PAGE = 0;
    private static final int SIZE_PAGE = 15;
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("booking-save")
    public String saveBooking(@SessionAttribute(name = "account") Account account,
                              @RequestParam(name = "firstDay") String firstDay,
                              @RequestParam(name = "lastDay") String lastDay,
                              @RequestParam(name = "comfort") String comfort,
                              @RequestParam(name = "placement") String placement) {
        bookingService.save(account, firstDay, lastDay, comfort, placement);
        return "redirect:/account";
    }

    @PostMapping("booking-delete")
    public String deleteBooking(@RequestParam(name = "booking_id") String id) {
        bookingService.delete(id);
        return "redirect:/account";
    }

    @PostMapping("booking-accept")
    public String acceptBooking(@RequestParam(name = "booking_id") String booking_id) {
        bookingService.acceptBooking(Long.parseLong(booking_id));
        return "redirect:/booking-all";
    }

    @GetMapping("booking-all")
    public String getAllBookings(@RequestParam(name = "page", required = false) String page, Model model) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, SIZE_PAGE);
        if (page != null && !page.isEmpty()) {
            pageable = PageRequest.of(Integer.parseInt(page), SIZE_PAGE);
        }

        List<Booking> all = bookingService.getAll(pageable);
        int maxPage = bookingService.count(pageable.getPageSize());

        model.addAttribute("bookings", all);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", maxPage);
        return "booking";
    }

    @GetMapping("booking-all-waiting")
    public String getAllWaiting(@RequestParam(name = "page", required = false) String page, Model model) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, SIZE_PAGE);
        if (page != null && !page.isEmpty()) {
            pageable = PageRequest.of(Integer.parseInt(page), SIZE_PAGE);
        }

        List<Booking> allWaiting = bookingService.getAllWaiting(pageable);
        int maxPage = bookingService.countWaiting(pageable.getPageSize());

        model.addAttribute("waiting", allWaiting);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", maxPage);
        return "waiting";
    }
}
