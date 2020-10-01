package com.tarasiuk.project.hotel.controller;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.UserRole;
import com.tarasiuk.project.hotel.service.AccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes(names = {"account", "page"})
@RequestMapping("/")
public class AccountController {

    private static final int DEFAULT_PAGE = 0;
    private static final int SIZE_PAGE = 15;
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
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

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("signup")
    public String registration(Account account) {
        accountService.save(account);
        return "redirect:/login";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("signin")
    public String login(Account account) {
        accountService.findByUsernameAndPassword(account);
        return "redirect:/account";
    }

    @PostMapping("change")
    public String changePassword(@SessionAttribute(name = "account") Account account,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "newPassword") String newPassword,
                                 @RequestParam(name = "repeatPassword") String repeatPassword) {
        accountService.changePassword(account, password, newPassword, repeatPassword);
        return "account";
    }

    @GetMapping("account")
    public String accountHome(@SessionAttribute(name = "account") Account account) {
        for (String role : account.getRoles()) {
            if (role.equals(UserRole.ADMIN.getRole())) {
                return "redirect:/clients";
            }
        }

        return "account";
    }

    @GetMapping("clients")
    public String getClients(@RequestParam(name = "page", required = false) String page, Model model) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, SIZE_PAGE);
        if (page != null && !page.isEmpty()) {
            pageable = PageRequest.of(Integer.parseInt(page), SIZE_PAGE);
        }

        List<Account> clients = accountService.getAll(pageable);
        int maxPage = accountService.count(pageable.getPageSize());

        model.addAttribute("clients", clients);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", maxPage);
        return "admin";
    }

    @PostMapping("block")
    public String blockClient(@RequestParam(name = "id") String client_id) {
        accountService.blocked(client_id);
        return "redirect:/clients";
    }
}
