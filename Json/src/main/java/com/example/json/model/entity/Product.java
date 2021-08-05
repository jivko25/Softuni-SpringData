package com.example.json.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BasicEntity{
    private String name;
    private Double price;
    private User buyer;
    private User seller;
    private Set<Category> category;


    public Product(){
    }


    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @ManyToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @ManyToOne
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @ManyToMany
    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
    }
}
