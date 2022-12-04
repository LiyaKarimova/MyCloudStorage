package com.liyakarimova.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessageCommand extends Command {

    private final String name;
    private final byte[] bytes;

    public FileMessageCommand(String name, byte [] bytes) throws IOException {
        this.name = name;
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }



    @Override
    public CommandType getType() {
        return CommandType.FILE_MESSAGE;
    }


}
