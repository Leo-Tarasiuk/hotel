package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.ClientPassport;
import com.tarasiuk.project.hotel.repository.ClientPassportRepository;
import com.tarasiuk.project.hotel.service.ClientPassportService;
import com.tarasiuk.project.hotel.service.exception.DataNotFoundException;
import com.tarasiuk.project.hotel.service.exception.ErrorCodes;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ClientPassportServiceImpl implements ClientPassportService {

    private final CustomLocaleResolver customLocaleResolver;
    private final ClientPassportRepository clientPassportRepository;

    public ClientPassportServiceImpl(ClientPassportRepository clientPassportRepository, CustomLocaleResolver customLocaleResolver) {
        this.clientPassportRepository = clientPassportRepository;
        this.customLocaleResolver = customLocaleResolver;
    }

    @Override
    public ClientPassport getByClient(Account account) {
        Optional<ClientPassport> clientPassport = clientPassportRepository.getClientPassportByAccount_Id(account.getId());
        return clientPassport.orElseThrow(() -> new DataNotFoundException(customLocaleResolver.messageSource()
                .getMessage("errorMessage.DataNotFoundException", null, LocaleContextHolder.getLocale()),
                ErrorCodes.NOT_FOUND.getErrorCode(), account.getId()));
    }

    @Override
    public ClientPassport save(String id, String name, String lastName, String patronymic, String country,
                               String dateOfBirth, String sex, String identification, String passportNum) {
        ClientPassport clientPassport = ClientPassport.builder()
                .name(name)
                .lastName(lastName)
                .passportNo(passportNum)
                .country(country)
                .dateOfBirth(convertToDate(dateOfBirth))
                .identificationNo(identification)
                .sex(sex)
                .patronymic(patronymic)
                .account(
                        Account.builder().id(Long.parseLong(id)).build()
                )
                .build();
        return clientPassportRepository.save(clientPassport);
    }

    @Override
    public List<ClientPassport> getAll() {
        return clientPassportRepository.findAll();
    }

    private LocalDate convertToDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return LocalDate.parse(date, dateTimeFormatter);
    }
}
