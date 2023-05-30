package com.baloot.service;

import com.baloot.exception.*;
import com.baloot.info.LoginInfo;
import com.baloot.info.RegisterInfo;
import com.baloot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final BalootSystem balootSystem;

    @Autowired
    public AuthService(BalootSystem balootSystem){
        this.balootSystem = balootSystem;
    }

    public  void authenticateUser(LoginInfo login) throws InValidInputException, UserNotFoundException {
        if (login.getUsername() == null)
            throw new InValidInputException("Username field cannot be empty");
        String name = login.getUsername();
        String password = login.getPassword();
        if (balootSystem.isUserValid(name))
            balootSystem.loginInUser(name, password);
        System.out.println(balootSystem.getLoggedInUser().getUsername() + " logged in");
    }

    public void logoutUser() throws Exception {
        balootSystem.logOutUser();
    }
    public  void registerUser(RegisterInfo signUpData) throws InValidInputException, UserNotFoundException {
        if(balootSystem.userExists(signUpData.getUsername()))
            throw new InValidInputException("Username is taken.");
        if(balootSystem.userExistsByEmail(signUpData.getEmail()))
            throw new InValidInputException("email is already registered.");
        User user = new User(signUpData.getUsername(), signUpData.getPassword(), signUpData.getEmail(), signUpData.getBirthDate(), signUpData.getAddress(), 0);
        balootSystem.addUser(user);
    }


}
