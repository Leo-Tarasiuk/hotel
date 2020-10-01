package com.tarasiuk.project.hotel.service;

import com.tarasiuk.project.hotel.model.Convenience;

import java.util.List;

public interface ConvenienceService {

    Convenience save(String name, String standard, String standardPlus, String deluxe,
                     String honeymoonRoom, String juniorSuite, String suite, String description);

    List<Convenience> getAll();
}
