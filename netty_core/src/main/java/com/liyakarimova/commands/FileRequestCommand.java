package com.liyakarimova.commands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class FileRequestCommand extends Command {

    private String fileName;

    public FileRequestCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public CommandType getType() {
        return CommandType.FILE_REQUEST;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
