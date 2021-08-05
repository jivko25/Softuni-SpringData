package com.example.football.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stats")
public class Stat {
    private Long id;
    private BigDecimal shooting;
    private BigDecimal passing;
    private BigDecimal endurance;

    public Stat() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getShooting() {
        return shooting;
    }

    public void setShooting(BigDecimal shooting) {
        this.shooting = shooting;
    }

    public BigDecimal getPassing() {
        return passing;
    }

    public void setPassing(BigDecimal passing) {
        this.passing = passing;
    }

    public BigDecimal getEndurance() {
        return endurance;
    }

    public void setEndurance(BigDecimal endurance) {
        this.endurance = endurance;
    }
}
