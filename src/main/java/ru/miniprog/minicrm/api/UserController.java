package ru.miniprog.minicrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.miniprog.minicrm.dto.UserCrmDTO;
import ru.miniprog.minicrm.model.UserCrm;
import ru.miniprog.minicrm.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserCrm> registerUser(@RequestBody UserCrmDTO user) {
        UserCrm registeredUser = new UserCrm();
        registeredUser.setUsername(user.getUsername());
        registeredUser.setPassword(user.getPassword());
        registeredUser.setEmail(user.getEmail());
        userService.registerUser(registeredUser);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
