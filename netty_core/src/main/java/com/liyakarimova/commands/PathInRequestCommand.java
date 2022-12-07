package com.liyakarimova.commands;

import lombok.Getter;

public class PathInRequestCommand extends Command {

    @Getter
    private String pathIn;

    @Override
    public CommandType getType() {
        return CommandType.PATH_IN_REQUEST;
    }

    public PathInRequestCommand(String pathIn) {
        this.pathIn = pathIn;
    }


}
