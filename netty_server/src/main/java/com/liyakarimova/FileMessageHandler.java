package com.liyakarimova;

import com.liyakarimova.commands.*;
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

    private Path currentPath;

    public FileMessageHandler() {
        this.currentPath = ROOT;
    }

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
                    log.info("Server started list request command");
                    ListResponseCommand listResponseCommand = new ListResponseCommand();
                    FilesInDirService filesInDirService = new FilesInDirService();
                    listResponseCommand.setFilesList(filesInDirService.findAllFilesInDir(ROOT));
                    listResponseCommand.setDirectoriesList(filesInDirService.findAllDirInDir(ROOT));
                    ctx.writeAndFlush(listResponseCommand);
                    log.info("Response list command sent");

                }

                case FILE_MESSAGE -> {
                    FileMessageCommand fileMessageCommand = (FileMessageCommand)cmd;
                    log.info("Server started file message command");
                    FileMessageService fileMessageService = new FileMessageService();
                    FileRequestCommand fileRequestCommand = new FileRequestCommand();
                    //System.err.println(fileMessageService.sendFile(fileMessageCommand.getName(), fileMessageCommand.getBytes(), currentPath.toString()));
                    fileRequestCommand.setFileMovedCorrect(fileMessageService.sendFile(fileMessageCommand.getName(),fileMessageCommand.getBytes(),currentPath.toString()));
                    ctx.writeAndFlush(fileRequestCommand);
                }

                case PATH_REQUEST -> {
                    log.info("Server PATH REQUEST COMMAND. Current dir: " + currentPath.toString());
                    PathResponseCommand pathResponseCommand = new PathResponseCommand();
                    pathResponseCommand.setCurrentPath(currentPath.toString());
                    ctx.writeAndFlush(pathResponseCommand);
                    log.info(pathResponseCommand.getCurrentPath().toString());

                }

                case PATH_UP_REQUEST -> {
                    if (!(currentPath.equals(ROOT))) {
                        currentPath = currentPath.getParent();
                        PathResponseCommand pathResponseCommand = new PathResponseCommand();
                        pathResponseCommand.setCurrentPath(currentPath.toString());
                        ctx.writeAndFlush(pathResponseCommand);
                    }
                }

            }
        }


    }

}
