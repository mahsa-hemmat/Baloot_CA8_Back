package com.baloot.repository;

import com.baloot.model.Rating;
import com.baloot.model.id.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    @Query("SELECT r.score FROM Rating r WHERE r.commodity.id = :commodityId")
    List<Integer> findRatingsByCommodity(@Param("commodityId") Integer commodityId);
}
