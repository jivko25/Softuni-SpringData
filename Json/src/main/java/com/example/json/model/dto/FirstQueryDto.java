package com.example.json.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FirstQueryDto {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;
    @Expose
    private UserSeedDto seller;

    public FirstQueryDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 500)
    @Max(value = 1000)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    public UserSeedDto getSeller() {
        return seller;
    }

    public void setSeller(UserSeedDto seller) {
        this.seller = seller;
    }
}
