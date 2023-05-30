package com.baloot.model;

import com.baloot.model.id.UserDiscountId;
import jakarta.persistence.*;


@Entity
@Table(name = "user_discount")
public class UserDiscount {
    @EmbeddedId
    private UserDiscountId userDiscountId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "discount_id", insertable = false, updatable = false)
    private Discount discount;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isUsed;
    public UserDiscount(){}

    public UserDiscountId getUserDiscountId() {
        return userDiscountId;
    }

    public void setUserDiscountId(UserDiscountId userDiscountId) {
        this.userDiscountId = userDiscountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}

