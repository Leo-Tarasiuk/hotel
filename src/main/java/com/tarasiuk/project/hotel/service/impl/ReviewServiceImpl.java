package com.tarasiuk.project.hotel.service.impl;

import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.model.Review;
import com.tarasiuk.project.hotel.repository.ReviewRepository;
import com.tarasiuk.project.hotel.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review save(Account account, String text) {
        Review review = Review.builder()
                .account(account)
                .review(text.trim())
                .build();
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAll(Pageable pageable) {
        return reviewRepository.findAll(pageable).getContent();
    }

    @Override
    public int count(int size) {
        return (int)(reviewRepository.count() / size);
    }
}
