package com.liyakarimova.services;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FilesInDirService {


    public List <String> findAllFilesInDir (Path root) {
        try {
            return  Files.list(root)
                    .filter(f -> resolveFileType(f) )
                    .map(f -> f.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("",e);
        }
        return null;
    }

    public List <String> findAllDirInDir (Path root) {
        try {
            return  Files.list(root)
                    .filter(f -> !resolveFileType(f) )
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("",e);
        }
        return null;
    }

    private  boolean resolveFileType(Path path) {
        if (Files.isDirectory(path)) {
            return false;
            //return String.format("%s\t%s", path.getFileName().toString(), "[DIR]");
        }
        return true;
    }
}
