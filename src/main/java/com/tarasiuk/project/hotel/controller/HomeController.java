package com.tarasiuk.project.hotel.controller;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Review;
import com.tarasiuk.project.hotel.model.Room;
import com.tarasiuk.project.hotel.service.AccountService;
import com.tarasiuk.project.hotel.service.ReviewService;
import com.tarasiuk.project.hotel.service.RoomService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes(names = {"account", "page"})
public class HomeController {

    private final AccountService accountService;
    private final RoomService roomService;
    private final ReviewService reviewService;
    private static final int DEFAULT_PAGE = 0;
    private static final int SIZE_PAGE = 5;

    public HomeController(RoomService roomService, AccountService accountService, ReviewService reviewService) {
        this.roomService = roomService;
        this.accountService = accountService;
        this.reviewService = reviewService;
    }

    @ModelAttribute
    public void home(Authentication authentication, Model model) {
        if (authentication != null) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            if (principal != null) {
                Account authenticated = accountService.findByUsername(principal.getUsername());
                model.addAttribute("account", authenticated);
            }
        }

        List<Room> rooms = roomService.getRooms();
        model.addAttribute("rooms", rooms);
    }

    @GetMapping("")
    public String main(@RequestParam(name = "page", required = false) String page, Model model) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, SIZE_PAGE);
        if (page != null && !page.isEmpty()) {
            pageable = PageRequest.of(Integer.parseInt(page), SIZE_PAGE);
        }

        List<Review> reviews = reviewService.getAll(pageable);
        int maxPage = reviewService.count(pageable.getPageSize());

        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("reviews", reviews);
        return "main";
    }

}
