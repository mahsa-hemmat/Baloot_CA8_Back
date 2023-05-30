package com.baloot.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserDiscountId implements Serializable {
    @Column(name = "user_id")
    private String username;

    @Column(name = "discount_id")
    private String discountId;

    public UserDiscountId() {
    }

    public UserDiscountId(String username, String discountId) {
        this.username = username;
        this.discountId = discountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username,discountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDiscountId userDiscountId)) return false;
        return Objects.equals(username, userDiscountId.username) &&
                Objects.equals(discountId, userDiscountId.discountId);
    }
}
