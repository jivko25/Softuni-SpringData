package com.example.springautomappingobjects.survice;

import com.example.springautomappingobjects.model.dto.UserLoginDto;
import com.example.springautomappingobjects.model.dto.UserRegisterDto;

public interface UserServece {
    String registerUser(UserRegisterDto userRegisterDto);
    String loginUser(UserLoginDto userLoginDto);
    String logOutUser();
}
