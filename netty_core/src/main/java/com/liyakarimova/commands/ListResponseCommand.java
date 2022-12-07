package com.liyakarimova.commands;

import com.liyakarimova.CloudItem;
import com.liyakarimova.services.FilesInDirService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ListResponseCommand  extends Command {

    private List<CloudItem> filesList;


    private String currentPath;




    public ListResponseCommand(String currentPath) {
        this.currentPath = currentPath;
        FilesInDirService filesInDirService = new FilesInDirService();
        this.filesList = filesInDirService.findAllFilesInDir(currentPath);
    }

    @Override
    public CommandType getType() {
        return CommandType.LIST_RESPONSE;
    }

    public List<CloudItem> getFilesList() {
        return filesList;
    }





}
