package com.baloot.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "commodity_id", referencedColumnName = "id")
    private Commodity commodity;
    private String text;
    private LocalDate date;
    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "dislike_count")
    private int dislikeCount;

    public Comment(){
        this.likeCount = 0;
        this.dislikeCount = 0;
    }
    public Comment(User user, Commodity commodity, String text){
        this.user = user;
        this.commodity = commodity;
        this.text = text;
        this.date = LocalDate.now();
        this.likeCount = 0;
        this.dislikeCount = 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate commentDate) {
        this.date = commentDate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
