package com.liyakarimova.Net;

import com.liyakarimova.AuthCommands.AuthInformation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthClientHandler extends SimpleChannelInboundHandler<AuthInformation> {

    private final CallbackAuth callback;

    public AuthClientHandler(CallbackAuth callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AuthInformation authInformation) throws Exception {
        log.info("Send auth info");
        callback.call(authInformation);

    }
}
