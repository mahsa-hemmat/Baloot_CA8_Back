package com.baloot.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "provider")
public class Provider {

    @Id
    @Column(name = "id")
    private Integer id;

    private String name;

    @Column(name = "registry_date")
    private String registryDate;

    @Column(length = 1024)
    private String image;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT '0.00'")
    private Double rating;

    @OneToMany(mappedBy = "provider", fetch = FetchType.EAGER)
    private List<Commodity> commodities = new ArrayList<>();


    public Provider() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(String registryDate) {
        this.registryDate = registryDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    public void addNewCommodity(Commodity newCommodity) {
        commodities.add(newCommodity);
    }
}
