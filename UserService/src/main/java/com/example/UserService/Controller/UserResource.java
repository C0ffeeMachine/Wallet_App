package com.example.UserService.Controller;

import com.example.UserService.Model.User;
import com.example.UserService.Repository.UserRepository;
import com.example.UserService.exception.UserNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class UserResource {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users")
    @ApiOperation(value = "Find all the users")
    List<User> findAll(){
        return userRepository.findAll();
    }

    @PostMapping(value = "/users")
    @ApiOperation(value = "Add a new user")
    @ResponseStatus(HttpStatus.CREATED)
    User newUser(User newUser){
        return userRepository.save(newUser);
    }

    @GetMapping(value = "/users/{id}")
    @ApiOperation(value = "Find a user by Id")
    User findById(@PathVariable int id){
        LOGGER.info("/users/{id} called with id "+ id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

}
