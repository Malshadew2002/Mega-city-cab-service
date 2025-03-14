package com.example.cabServiceMegaCity.components;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.cabServiceMegaCity.models.UserModel;

@Component
@SessionScope
public class LoggedUserData {
    private UserModel userModel;

    public UserModel getLoggedUser() {
        return userModel;
    }

    public void setLoggedUser(UserModel userModel) {
        this.userModel = userModel;
    }
}
