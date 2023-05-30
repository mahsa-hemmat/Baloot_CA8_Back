package com.baloot.repository;

import com.baloot.model.Commodity;
import com.baloot.model.Discount;
import com.baloot.model.User;
import com.baloot.model.UserDiscount;
import com.baloot.model.id.UserDiscountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDiscountRepository extends JpaRepository<UserDiscount, UserDiscountId> {

    @Query(value = "SELECT ud.isUsed FROM UserDiscount ud WHERE ud.user = ?1 AND ud.discount = ?2")
    boolean isDiscountUsedByUser(User user, Discount discount);
}
