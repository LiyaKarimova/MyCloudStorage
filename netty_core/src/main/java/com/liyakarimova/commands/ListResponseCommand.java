package com.liyakarimova.commands;

import com.liyakarimova.commands.Command;
import com.liyakarimova.commands.CommandType;

public class ListResponseCommand  extends Command {

    private String fileList;

    @Override
    public CommandType getType() {
        return CommandType.LIST_RESPONSE;
    }

    public String getFileList() {
        return fileList;
    }

    public void setFileList(String fileList) {
        this.fileList = fileList;
    }
}
