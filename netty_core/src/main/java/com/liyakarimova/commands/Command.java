package com.liyakarimova.commands;

import java.io.Serializable;

public class Command implements Serializable {

    CommandType type;

    public CommandType getType() {
        return type;
    }
}
