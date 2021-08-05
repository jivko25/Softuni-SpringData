package com.example.json.service.impl;

import com.example.json.constants.GlobalConstants;
import com.example.json.model.dto.UserSeedDto;
import com.example.json.model.entity.User;
import com.example.json.repository.UserRepo;
import com.example.json.service.UserService;
import com.example.json.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedUser() throws IOException {
        if(userRepo.count() > 0){
            return;
        }
//        Arrays.stream(gson.fromJson(
//                Files.readString(Path.of(GlobalConstants.usersPath)), UserSeedDto[].class))
//                .filter(validationUtil::isValid)
//                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
//                .forEach(userRepo::save);
        String fileContent = Files
                .readString(Path.of(GlobalConstants.usersPath));

        UserSeedDto[] userSeedDtos = gson
                .fromJson(fileContent, UserSeedDto[].class);

        Arrays.stream(userSeedDtos)
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepo::save);
    }

    @Override
    public User getRandomUser() {
        Long randomId = ThreadLocalRandom
                .current().nextLong(1,
                        userRepo.count() + 1);

        User user = userRepo
                .findById(randomId)
                .orElse(null);
        return user;
    }
}
