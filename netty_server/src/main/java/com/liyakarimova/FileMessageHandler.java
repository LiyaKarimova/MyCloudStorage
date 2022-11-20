package com.liyakarimova;

import com.liyakarimova.commands.Command;
import com.liyakarimova.commands.FileMessageCommand;
import com.liyakarimova.commands.FileRequestCommand;
import com.liyakarimova.commands.ListResponseCommand;
import com.liyakarimova.services.FileMessageService;
import com.liyakarimova.services.FilesInDirService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileMessageHandler extends SimpleChannelInboundHandler<Command> {

    private static final Path ROOT = Paths.get("netty_server", "root");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command cmd) throws Exception {
////        Files.write(
////                ROOT.resolve(fileMessage.getName()),
////                fileMessage.getBytes()
////        );
//
//
//
//        ctx.writeAndFlush("OK");
        if (cmd.getType() == null) {
            log.error("Command type is null");
        } else {
            switch (cmd.getType()) {
                case LIST_REQUEST -> {
                    log.info("Server started list request comand");
                    ListResponseCommand listResponseCommand = new ListResponseCommand();
                    FilesInDirService filesInDirService = new FilesInDirService();
                    listResponseCommand.setFileList(filesInDirService.findAllFilesInDir(ROOT));
                    ctx.writeAndFlush(listResponseCommand);
                    log.info("Response list command sent");

                }

                case FILE_MESSAGE -> {

                    FileMessageCommand fileMessageCommand = (FileMessageCommand)cmd;
                    log.info("Server started file message command");
                    FileMessageService fileMessageService = new FileMessageService();
                    FileRequestCommand fileRequestCommand = new FileRequestCommand();
                    fileRequestCommand.setFileMovedCorrect(fileMessageService.sendFile(Paths.get(fileMessageCommand.getFilePath()),Paths.get(fileMessageCommand.getToDir())));
                    ctx.writeAndFlush(new FileRequestCommand());
                }

            }
        }


    }

}
