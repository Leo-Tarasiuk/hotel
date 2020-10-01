package com.tarasiuk.project.hotel.service;

import com.tarasiuk.project.hotel.model.Account;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {

    Account findByUsername(String username);

    List<Account> getAll(Pageable pageable);

    int count(int size);

    Account save(Account account);

    Account changePassword(Account account, String password, String newPassword, String repeatPassword);

    Account findByUsernameAndPassword(Account account);

    boolean blocked(String client_id);
}
