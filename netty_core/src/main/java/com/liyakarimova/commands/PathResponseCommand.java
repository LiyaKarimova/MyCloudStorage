package com.liyakarimova.commands;

import java.nio.file.Path;

public class PathResponseCommand extends Command {

    private String currentPath;

    public PathResponseCommand(String currentPath) {
        this.currentPath = currentPath;
    }

    @Override
    public CommandType getType() {
        return CommandType.PATH_RESPONSE;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }
}
