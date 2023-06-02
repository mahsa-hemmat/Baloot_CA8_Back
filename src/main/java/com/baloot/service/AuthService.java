package com.baloot.service;

import com.baloot.exception.*;
import com.baloot.info.LoginInfo;
import com.baloot.info.RegisterInfo;
import com.baloot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.boot.model.source.internal.hbm.Helper.extractParameters;


@Service
public class AuthService {
    private final BalootSystem balootSystem;

    @Autowired
    public AuthService(BalootSystem balootSystem){
        this.balootSystem = balootSystem;
    }

    public  String authenticateUser(LoginInfo login) throws InValidInputException, UserNotFoundException {
        if (login.getUsername() == null)
            throw new InValidInputException("Username field cannot be empty");
        String name = login.getUsername();
        String password = login.getPassword();
        System.out.println(balootSystem.isUserValid(name));
        if (balootSystem.isUserValid(name))
            balootSystem.loginInUser(name, password);
        System.out.println(balootSystem.getLoggedInUser().getUsername() + " logged in");
        return JWTService.getInstance().createJWT(login.getUsername());
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


    public String loginGithub(String githubCode) throws Exception{
        URL url = new URL("https://github.com/login/oauth/access_token?client_id=1ed1d4c744ddbb6cffea&client_secret=8ee8eb33be4f4421d43705fe4364c82932b2e250&code="+githubCode);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        Map<String, String> parameters = extractParameters(content.toString());
        String name=balootSystem.setUpUser(parameters.get("access_token"));
        return JWTService.getInstance().createJWT(name);
    }
    private static Map<String, String> extractParameters(String input) throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();

        String[] keyValuePairs = input.split("&");
        for (String pair : keyValuePairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                String key = URLDecoder.decode(parts[0], "UTF-8");
                String value = URLDecoder.decode(parts[1], "UTF-8");
                parameters.put(key, value);
            }
        }

        return parameters;
    }
}
