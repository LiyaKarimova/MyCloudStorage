package com.liyakarimova.Net;

import com.liyakarimova.commands.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler <Command> {

    private final Callback callback;


    public ClientHandler(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

    }
}
