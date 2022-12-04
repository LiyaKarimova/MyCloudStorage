package com.liyakarimova.commands;

import java.util.List;

public class ListResponseCommand  extends Command {

    private List<String> filesList;

    private List <String> directoriesList;




    @Override
    public CommandType getType() {
        return CommandType.LIST_RESPONSE;
    }

    public List<String> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<String> filesList) {
        this.filesList = filesList;
    }

    public List<String> getDirectoriesList() {
        return directoriesList;
    }

    public void setDirectoriesList(List<String> directoriesList) {
        this.directoriesList = directoriesList;
    }
}
