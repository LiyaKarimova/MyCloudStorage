package com.liyakarimova.Net;

import com.liyakarimova.commands.Command;

public interface Callback {

    void call (Command cmd);
    //void callAuth(AuthInformation authInformation);
}

