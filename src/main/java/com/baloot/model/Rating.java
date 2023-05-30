package com.baloot.model;

import com.baloot.model.id.RatingId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Rating {
    @EmbeddedId
    private RatingId id;
    @ManyToOne
    @MapsId("user_id")
    private User user;

    @ManyToOne
    @MapsId("commodity_id")
    private Commodity commodity;

    private double score;
    public Rating(){

    }
    public Rating(User user, Commodity commodity ,double score){
        this.id = new RatingId(user.getUsername(), commodity.getId());
        this.user = user;
        this.commodity = commodity;
        this.score = score;
    }

    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
