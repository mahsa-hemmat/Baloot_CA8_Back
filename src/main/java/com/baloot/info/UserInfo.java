package com.baloot.info;

import com.baloot.model.User;

public class UserInfo {
    private final String username;
    private final String email;
    private final String birthDate;
    private final String address;
    private final int credit;

    public UserInfo(User user){
        username = user.getUsername();
        email = user.getEmail();
        birthDate = user.getBirthDate().replace("-", "/");
        address = user.getAddress();
        credit = user.getCredit();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public int getCredit() {
        return credit;
    }
}
