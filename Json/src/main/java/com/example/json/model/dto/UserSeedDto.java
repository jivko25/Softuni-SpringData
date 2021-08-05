package com.example.json.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class UserSeedDto {
    @Expose
    private int age;
    @Expose
    private String firstName;
    @Expose
    private String lastName;

    public UserSeedDto(){
    }

    public String getFirst_name() {
        return firstName;
    }

    public void setFirst_name(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 3)
    public String getLast_name() {
        return lastName;
    }

    public void setLast_name(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
