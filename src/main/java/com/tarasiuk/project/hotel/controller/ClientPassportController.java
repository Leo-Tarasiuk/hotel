package com.tarasiuk.project.hotel.controller;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.ClientPassport;
import com.tarasiuk.project.hotel.service.AccountService;
import com.tarasiuk.project.hotel.service.ClientPassportService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ClientPassportController {

    private final AccountService accountService;
    private final ClientPassportService clientPassportService;

    public ClientPassportController(ClientPassportService clientPassportService, AccountService accountService) {
        this.clientPassportService = clientPassportService;
        this.accountService = accountService;
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
    }

    @PostMapping("passport-save")
    public String savePassport(@RequestParam(name = "id") String id,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "lastName") String lastName,
                               @RequestParam(name = "patronymic") String patronymic,
                               @RequestParam(name = "country") String country,
                               @RequestParam(name = "birthday") String dateOfBirth,
                               @RequestParam(name = "sex") String sex,
                               @RequestParam(name = "identification") String identification,
                               @RequestParam(name = "passport") String passportNum) {
        clientPassportService.save(id, name, lastName, patronymic, country,
                dateOfBirth, sex, identification, passportNum);
        return "redirect:/account";
    }

    @GetMapping("passport-account")
    public String getPassport(Account account, Model model) {
        ClientPassport passport = clientPassportService.getByClient(account);
        model.addAttribute("passport", passport);
        return "account";
    }

}
