package com.liyakarimova.services;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Slf4j
public class FilesInDirService {


    public String findAllFilesInDir (Path root) {
        try {
            String s = Files.list(root)
                    .map(this::resolveFileType)
                    .collect(Collectors.joining("\n")) + "\n";
            return s;
        } catch (IOException e) {
            log.error("",e);
        }
        return null;
    }

    private  String resolveFileType(Path path) {
        if (Files.isDirectory(path)) {
            return String.format("%s\t%s", path.getFileName().toString(), "[DIR]");
        } else {
            return String.format("%s\t%s", path.getFileName().toString(), "[FILE]");
        }
    }
}
