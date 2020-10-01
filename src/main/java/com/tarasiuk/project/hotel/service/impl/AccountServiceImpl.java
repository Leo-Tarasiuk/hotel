package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.UserRole;
import com.tarasiuk.project.hotel.repository.AccountRepository;
import com.tarasiuk.project.hotel.service.AccountService;
import com.tarasiuk.project.hotel.service.exception.ChangePasswordException;
import com.tarasiuk.project.hotel.service.exception.DataNotFoundException;
import com.tarasiuk.project.hotel.service.exception.ErrorCodes;
import com.tarasiuk.project.hotel.service.exception.InvalidLoginException;
import com.tarasiuk.project.hotel.service.exception.SaveException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AccountServiceImpl implements AccountService {

    private static final String PATTERN_LOGIN = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    private final CustomLocaleResolver customLocaleResolver;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, CustomLocaleResolver customLocaleResolver) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.customLocaleResolver = customLocaleResolver;
    }

    @Override
    public Account findByUsername(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.orElseThrow(() -> new DataNotFoundException(customLocaleResolver.messageSource()
                .getMessage("errorMessage.DataNotFoundException", null, LocaleContextHolder.getLocale()),
                ErrorCodes.NOT_FOUND.getErrorCode(), username));
    }

    @Override
    public List<Account> getAll(Pageable pageable) {
        List<Account> accounts = accountRepository.findAll(pageable).getContent();
        accounts = accounts.stream()
                .filter(account -> account.getClientPassport() != null)
                .collect(Collectors.toList());
        return accounts;
    }

    @Override
    public int count(int size) {
        return (int) (accountRepository.count() / size);
    }

    @Override
    public Account save(Account account) {
        if (!account.getUsername().matches(PATTERN_LOGIN)) {
            throw new SaveException(customLocaleResolver.messageSource().getMessage("errorMessage.SaveException"
                    , null, LocaleContextHolder.getLocale()), ErrorCodes.SAVE_EXCEPTION.getErrorCode());
        }

        Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
        if (optionalAccount.isPresent()) {
            throw new SaveException(customLocaleResolver.messageSource().getMessage("errorMessage.SaveException"
                    , null, LocaleContextHolder.getLocale()), ErrorCodes.SAVE_EXCEPTION.getErrorCode());
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setActive(1);
        account.setRoles(UserRole.CLIENT.getRole());
        return accountRepository.save(account);
    }

    @Override
    public Account changePassword(Account account, String password, String newPassword, String repeatPassword) {
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new InvalidLoginException(customLocaleResolver.messageSource()
                    .getMessage("errorMessage.InvalidLoginException", null, LocaleContextHolder.getLocale()),
                    ErrorCodes.INVALID_LOGIN_EXCEPTION.getErrorCode());
        }
        if (!newPassword.equals(repeatPassword)) {
            throw new ChangePasswordException(customLocaleResolver.messageSource()
                    .getMessage("errorMessage.ChangePasswordException", null, LocaleContextHolder.getLocale()),
                    ErrorCodes.CHANGE_PASSWORD.getErrorCode());
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        return accountRepository.save(account);
    }

    @Override
    public Account findByUsernameAndPassword(Account account) {
        Optional<Account> accountOptional = accountRepository
                .findByUsernameAndPassword(account.getUsername(), account.getPassword());

        return accountOptional.orElseThrow(() -> new DataNotFoundException(customLocaleResolver.messageSource()
                .getMessage("errorMessage.DataNotFoundException", null, LocaleContextHolder.getLocale()),
                ErrorCodes.NOT_FOUND.getErrorCode(), account.getUsername()));
    }

    @Override
    public boolean blocked(String client_id) {
        accountRepository.changeActive(Long.parseLong(client_id));
        return true;
    }
}
