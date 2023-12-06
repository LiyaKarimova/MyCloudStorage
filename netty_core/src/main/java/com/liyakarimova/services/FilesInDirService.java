package com.liyakarimova.services;

import com.liyakarimova.CloudItem;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FilesInDirService {


    public List <CloudItem> findAllFilesInDir (String root) {
        try {
            return  Files.list(Paths.get(root))
                    .map(f -> new CloudItem(f.getFileName().toString(),resolveFileType(f)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("",e);
        }
        return null;
    }

    private  boolean resolveFileType(Path path) {
        if (Files.isDirectory(path)) {
            return true;
            //return String.format("%s\t%s", path.getFileName().toString(), "[DIR]");
        }
        return false;
    }
}
