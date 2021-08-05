package com.example.springautomappingobjects.survice.impl;

import com.example.springautomappingobjects.model.dto.GameAddDto;
import com.example.springautomappingobjects.model.entity.Games;
import com.example.springautomappingobjects.model.entity.Role;
import com.example.springautomappingobjects.model.entity.User;
import com.example.springautomappingobjects.repository.GameRepository;
import com.example.springautomappingobjects.repository.UserRepository;
import com.example.springautomappingobjects.survice.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;
    private String loggedInUser;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public String addGame(GameAddDto gameAddDto) {
        StringBuilder sb = new StringBuilder();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Games game = this.modelMapper.map(gameAddDto, Games.class);
        Set<ConstraintViolation<Object>> violations = validator.validate(game);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Object> violation : violations) {
                sb.append(violation.getMessage()).append(System.lineSeparator());
            }
            return sb.toString();
        }

        User user = this.userRepository.findByEmail(this.loggedInUser).orElse(null);

        if (!user.getRole().equals(Role.ADMIN)) {
            return sb.append(String.format("%s is not admin !", user.getFullName())).toString();
        }

        this.gameRepository.saveAndFlush(game);

        Set<Games> games = user.getGames();
        games.add(game);
        user.setGames(games);

        this.userRepository.saveAndFlush(user);

        sb.append(String.format("Added %s", game.getTitle()));
        return sb.toString();
    }

    @Override
    public void setLoggedInUser(String email) {
        this.loggedInUser = email;
    }

    @Override
    public void logOutUser() {
        this.loggedInUser = "";
    }
}
