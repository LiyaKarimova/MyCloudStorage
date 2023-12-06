package com.liyakarimova;

import com.liyakarimova.AuthCommands.AuthInformation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthHandler extends SimpleChannelInboundHandler <AuthInformation> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AuthInformation authInformation) throws Exception {
        log.info("Auth handler");

    }
}
