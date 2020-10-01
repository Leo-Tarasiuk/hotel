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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class RoomController {

    private final AccountService accountService;
    private final RoomService roomService;
    private final ConvenienceService convenienceService;

    public RoomController(RoomService roomService, AccountService accountService, ConvenienceService convenienceService) {
        this.roomService = roomService;
        this.accountService = accountService;
        this.convenienceService = convenienceService;
    }

    @ModelAttribute
    public void room(Authentication authentication, Model model) {
        if (authentication != null) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            if (principal != null) {
                Account authenticated = accountService.findByUsername(principal.getUsername());
                model.addAttribute("account", authenticated);
            }
        }

        List<Room> rooms = roomService.getRooms();
        List<Convenience> conveniences = convenienceService.getAll();
        model.addAttribute("rooms", rooms);
        model.addAttribute("conveniences", conveniences);
    }

    @GetMapping("room")
    public String getRoom(@RequestParam(name = "id") String room_id, Model model) {
        Room room = roomService.getById(room_id);
        model.addAttribute("room", room);
        return "room";
    }

}
