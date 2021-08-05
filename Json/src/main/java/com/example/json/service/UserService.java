package com.example.json.service;

import com.example.json.model.entity.User;

import java.io.IOException;

public interface UserService {
    public void seedUser() throws IOException;
    public User getRandomUser() throws IOException;
}
