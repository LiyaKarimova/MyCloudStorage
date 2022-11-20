package com.liyakarimova.services;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileMessageService {

    public boolean sendFile (Path fromPath, Path toPath) throws IOException {
        try{
            log.debug("Server started sending file");
            Path newPath = Paths.get(toPath.toString(),fromPath.getFileName().toString());
            if (!Files.exists(newPath)) {
                Files.createFile(newPath);
            }
            Files.write(
                    newPath,
                    Files.readAllBytes(fromPath));
            return true;

        } catch (Exception e) {
            log.error("Не смог переместить файл!", e);
            return false;
        }


    }
}
