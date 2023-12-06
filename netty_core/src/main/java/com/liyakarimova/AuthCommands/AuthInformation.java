package com.liyakarimova.AuthCommands;

import java.io.Serializable;

public class AuthInformation extends AuthCommand  {

    private String login;

    private String password;

    public AuthInformation(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
