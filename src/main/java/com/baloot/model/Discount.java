package com.baloot.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @Column(name = "id")
    private String discountCode;
    private Integer discount;
    @ManyToMany(mappedBy = "discounts")
    private List<User> users = new ArrayList<>();

    public Discount(){}

    public Discount(String discountCode, Integer discount){
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
