package com.tarasiuk.project.hotel.service;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    Review save(Account account, String text);

    List<Review> getAll(Pageable pageable);

    int count(int size);
}
