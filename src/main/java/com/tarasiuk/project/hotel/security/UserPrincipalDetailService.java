package com.tarasiuk.project.hotel.security;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPrincipalDetailService implements UserDetailsService {

    private AccountRepository accountRepository;

    public UserPrincipalDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = this.accountRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal(account);

        return userPrincipal;
    }
}
