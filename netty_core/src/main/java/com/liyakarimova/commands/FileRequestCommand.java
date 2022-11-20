package com.liyakarimova.commands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class FileRequestCommand extends Command {

    private boolean fileMovedCorrect;

    @Override
    public CommandType getType() {
        return CommandType.FILE_REQUEST;
    }

    public boolean isFileMovedCorrect() {
        return fileMovedCorrect;
    }

    public void setFileMovedCorrect(boolean fileMovedCorrect) {
        this.fileMovedCorrect = fileMovedCorrect;
    }
}
