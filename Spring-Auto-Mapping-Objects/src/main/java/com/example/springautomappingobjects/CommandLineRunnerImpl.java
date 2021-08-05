package com.example.springautomappingobjects;

import com.example.springautomappingobjects.model.dto.GameAddDto;
import com.example.springautomappingobjects.model.dto.UserLoginDto;
import com.example.springautomappingobjects.model.dto.UserRegisterDto;
import com.example.springautomappingobjects.survice.GameService;
import com.example.springautomappingobjects.survice.UserServece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final UserServece userService;
    private final GameService gameService;

    @Autowired
    public CommandLineRunnerImpl(UserServece userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true){
            String[] data = scanner.nextLine().split("\\|");

            switch (data[0]){
                case "RegisterUser":
                    UserRegisterDto userRegisterDto = new UserRegisterDto(data[1],data[2],data[3],data[4]);
                    System.out.println(this.userService.registerUser(userRegisterDto));
                    break;
                case "LoginUser":
                    UserLoginDto userLoginDto = new UserLoginDto(data[1],data[2]);
                    System.out.println(this.userService.loginUser(userLoginDto));
                    this.gameService.setLoggedInUser(userLoginDto.getEmail());
                    break;
                case "Logout":
                    System.out.println(this.userService.logOutUser());
                    this.gameService.logOutUser();
                    break;
                case "AddGame":

                    GameAddDto gameAddDto = new GameAddDto(
                            data[1],data[4],data[5], Double.parseDouble(data[3]),
                            new BigDecimal(data[2]), data[6], LocalDate.parse(data[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    );
                    System.out.println(this.gameService.addGame(gameAddDto));
                    break;
                case "EditGame":
                    break;
                case "DeleteGame":
                    break;
            }
        }
    }
}
