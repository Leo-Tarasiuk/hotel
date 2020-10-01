package com.tarasiuk.project.hotel.controller;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = {"account"})
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review-add")
    public String save(@SessionAttribute(name = "account")Account account, String text) {
        reviewService.save(account, text);
        return "redirect:/";
    }
}
