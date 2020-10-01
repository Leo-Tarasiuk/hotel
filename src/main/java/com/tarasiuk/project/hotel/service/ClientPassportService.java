package com.tarasiuk.project.hotel.service;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.ClientPassport;

import java.time.Instant;
import java.util.List;

public interface ClientPassportService {
    ClientPassport getByClient(Account account);
    ClientPassport save(String id, String name, String lastName, String patronymic, String country,
                        String dateOfBirth, String sex, String identification, String passportNum);
    List<ClientPassport> getAll();
}
