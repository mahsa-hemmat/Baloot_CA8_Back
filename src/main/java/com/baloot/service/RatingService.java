package com.baloot.service;

import com.baloot.model.Rating;
import com.baloot.model.id.RatingId;
import com.baloot.repository.RatingRepository;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository repo;
    @Autowired
    public RatingService(RatingRepository ratingRepository){
        this.repo = ratingRepository;
    }
    public void save(Rating rating){
        repo.save(rating);
    }
    public Rating findRatingById(RatingId ratingId){
        return repo.findById(ratingId).orElse(null);
    }

    public List<Integer> calculateRating(Integer commodityId) {
        return repo.findRatingsByCommodity(commodityId);
    }
}
