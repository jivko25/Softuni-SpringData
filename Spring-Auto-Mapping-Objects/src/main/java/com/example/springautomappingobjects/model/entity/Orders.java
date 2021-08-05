package com.example.springautomappingobjects.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Orders extends BaseEntity{
    private User buyer;
    private Set<Games> games;

    public Orders(){
    }

    @ManyToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @ManyToMany
    public Set<Games> getGames() {
        return games;
    }

    public void setGames(Set<Games> games) {
        this.games = games;
    }
}
