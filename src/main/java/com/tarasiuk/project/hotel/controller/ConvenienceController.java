package com.tarasiuk.project.hotel.controller;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Convenience;
import com.tarasiuk.project.hotel.model.Room;
import com.tarasiuk.project.hotel.service.AccountService;
import com.tarasiuk.project.hotel.service.ConvenienceService;
import com.tarasiuk.project.hotel.service.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class ConvenienceController {

    private final AccountService accountService;
    private final ConvenienceService convenienceService;
    private final RoomService roomService;

    public ConvenienceController(ConvenienceService convenienceService, AccountService accountService, RoomService roomService) {
        this.convenienceService = convenienceService;
        this.accountService = accountService;
        this.roomService = roomService;
    }

    @ModelAttribute
    public void authenticated(Authentication authentication, Model model) {
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

    @PostMapping("convenience-save")
    public String saveConvenience(@RequestParam(name = "name") String name,
                                  @RequestParam(name = "standart") String standartKing,
                                  @RequestParam(name = "standart_plus") String standartTwin,
                                  @RequestParam(name = "deluxe") String deluxe,
                                  @RequestParam(name = "family_suite") String familySuite,
                                  @RequestParam(name = "junior_suite") String juniorSuite,
                                  @RequestParam(name = "suite") String cornerSuite,
                                  @RequestParam(name = "description") String description) {
        convenienceService.save(name, standartKing, standartTwin, deluxe,
                familySuite, juniorSuite, cornerSuite, description);
        return "convenience";
    }

    @GetMapping("convenience-all")
    public String getAllConveniences(Model model) {
        List<Convenience> conveniences = convenienceService.getAll();
        model.addAttribute("conveniences", conveniences);
        return "convenience";
    }

    @GetMapping("fitness")
    public String getFitness() {
        return "fitness";
    }

    @GetMapping("spa")
    public String getSpa() {
        return "spa";
    }

    @GetMapping("map")
    public String getMap() {
        return "map";
    }

    @GetMapping("dining")
    public String getDining() {
        return "dining";
    }
}
