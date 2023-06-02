package com.baloot.controller;

import com.baloot.exception.InValidInputException;
import com.baloot.exception.UserNotFoundException;
import com.baloot.info.LoginInfo;
import com.baloot.info.RegisterInfo;
import com.baloot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginInfo loginData) throws UserNotFoundException, InValidInputException {
        try {
            String jwt=authService.authenticateUser(loginData);
            return ResponseEntity.status(HttpStatus.OK).body("{ \"jwt\" :\""+jwt+"\"}");
        } catch (UserNotFoundException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InValidInputException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        try {
            authService.logoutUser();
            return ResponseEntity.status(HttpStatus.OK).body("logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterInfo registerData) {
        try {
            authService.registerUser(registerData);
            System.out.println("signed up successfully");
            return ResponseEntity.status(HttpStatus.OK).body("Registered successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Register Failed." + e.getMessage());
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<Object> callback(@RequestBody String githubCode) {
        try {
            System.out.println("signed up successfully");
            return ResponseEntity.status(HttpStatus.OK).body("Registered successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Register Failed." + e.getMessage());
        }
    }
}
