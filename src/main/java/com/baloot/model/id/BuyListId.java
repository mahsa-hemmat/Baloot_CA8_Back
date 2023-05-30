package com.baloot.model.id;

import com.baloot.model.BuyList;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BuyListId implements Serializable {
    @Column(name = "user_id")
    private String username;

    @Column(name = "commodity_id")
    private Integer commodityId;

    public BuyListId() {
    }

    public BuyListId(String username, Integer commodityId) {
        this.username = username;
        this.commodityId = commodityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username,commodityId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuyListId buyListId)) return false;
        return Objects.equals(username, buyListId.username) &&
                Objects.equals(commodityId, buyListId.commodityId);
    }
}

