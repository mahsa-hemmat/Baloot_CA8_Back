package com.baloot.model;

import com.baloot.model.id.HistoryListId;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "historylist")
public class HistoryList {
    @EmbeddedId
    private HistoryListId id;

    @ManyToOne
    @MapsId("user_id")
    private User user;

    @ManyToOne
    @MapsId("commodity_id")
    private Commodity commodity;
    private int quantity;
    public HistoryList(User user, Commodity commodity, int quantity){
        this.id = new HistoryListId(user.getUsername(), commodity.getId());
        this.user = user;
        this.commodity = commodity;
        this.quantity = quantity;
    }


    public HistoryList() {
    }

    public HistoryListId getId() {
        return id;
    }

    public void setId(HistoryListId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryList)) return false;
        HistoryList histlist = (HistoryList) o;
        return Objects.equals(id, histlist.id) &&
                Objects.equals(user, histlist.user)&&
                Objects.equals(commodity, histlist.commodity) &&
                Objects.equals(quantity, histlist.quantity);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, user, commodity, quantity);
    }
}
