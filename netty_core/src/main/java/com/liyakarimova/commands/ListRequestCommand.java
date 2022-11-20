package com.liyakarimova.commands;

import com.liyakarimova.commands.Command;
import com.liyakarimova.commands.CommandType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListRequestCommand extends Command {

    @Override
    public CommandType getType() {
        return CommandType.LIST_REQUEST;
    }

}
