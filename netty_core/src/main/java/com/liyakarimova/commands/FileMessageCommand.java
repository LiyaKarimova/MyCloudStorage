package com.liyakarimova.commands;

import java.nio.file.Path;

public class FileMessageCommand extends Command {

    private String filePath;
    private String toDir;

    public FileMessageCommand(String filePath, String toDir) {
        this.filePath = filePath;
        this.toDir = toDir;
    }

    @Override
    public CommandType getType() {
        return CommandType.FILE_MESSAGE;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getToDir() {
        return toDir;
    }

    //    public Path getFilePath() {
//        return filePath;
//    }
//
//    public Path getToDir() {
//        return toDir;
//    }
}
