package com.example.springautomappingobjects.survice.impl;

import com.example.springautomappingobjects.model.dto.UserLoginDto;
import com.example.springautomappingobjects.model.dto.UserRegisterDto;
import com.example.springautomappingobjects.model.entity.Role;
import com.example.springautomappingobjects.model.entity.User;
import com.example.springautomappingobjects.repository.UserRepository;
import com.example.springautomappingobjects.survice.UserServece;
import com.example.springautomappingobjects.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class UserServeceImpl implements UserServece {
    private final UserRepository userRepository;
    private ModelMapper modelMapper;
    private String loggedInUser;

    @Autowired
    public UserServeceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        this.loggedInUser = "";
    }

    @Override
    public String registerUser(UserRegisterDto userRegisterDto) {
        StringBuilder sb = new StringBuilder();

        User user = this.modelMapper.map(userRegisterDto, User.class);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        User inDb = this.userRepository.findByEmail(user.getEmail()).orElse(null);

        if(inDb != null){
            return sb.append("User is already registered").toString();
        }

        if(!violations.isEmpty()){
            for (ConstraintViolation<User> violation : violations) {
                sb.append(violation.getMessage()).append(System.lineSeparator());
            }
        } else {

            if (this.userRepository.count() == 0){
                user.setRole(Role.ADMIN);
            } else{
                user.setRole(Role.USER);
            }

            sb.append(String.format("%s was registered.",user.getFullName()));
            this.userRepository.saveAndFlush(user);
        }

        return sb.toString();
    }

    @Override
    public  String loginUser(UserLoginDto userLoginDto){
        StringBuilder sb = new StringBuilder();

        if(!this.loggedInUser.isEmpty()){
            return sb.append("User is already logged in.").toString();
        }

        User user = this.userRepository.findByEmail(userLoginDto.getEmail()).orElse(null);

        if(user == null){
            return sb.append("Incorrect email").toString();
        } else {
            if(!user.getPassword().equals(userLoginDto.getPassword())){
                return sb.append("Incorrect password !").toString();
            }

            this.loggedInUser = user.getEmail();
            sb.append(String.format("Successfully logged in %s", user.getFullName()));
        }

        return sb.toString();
    }

    @Override
    public String logOutUser() {
        StringBuilder sb = new StringBuilder();

        if(this.loggedInUser.isEmpty()){
            sb.append("Cannot log out. No user was logged in.");
        } else {
            User user = this.userRepository.findByEmail(this.loggedInUser).orElse(null);
            sb.append(String.format("User %s successfully logged out.",user.getFullName()));
            this.loggedInUser = "";
        }

        return sb.toString();
    }
}
