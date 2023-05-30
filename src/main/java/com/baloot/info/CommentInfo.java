package com.baloot.info;

import java.time.LocalDate;

public class CommentInfo {
    private Long id;
    private String userEmail;
    private int commodityId;
    private String text;
    private String date;
    private int like;
    private int dislike;
    public CommentInfo(){

    }
    public CommentInfo(Long id,String userEmail, int commodityId, String text, String date, int
                       like, int dislike) {
        this.id = id;
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
        this.like = like;
        this.dislike = dislike;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }
}
