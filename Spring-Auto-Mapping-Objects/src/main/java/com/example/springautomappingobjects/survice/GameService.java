package com.example.springautomappingobjects.survice;

import com.example.springautomappingobjects.model.dto.GameAddDto;

public interface GameService {
    String addGame(GameAddDto gameAddDto);
    void setLoggedInUser(String email);
    void logOutUser();
}
