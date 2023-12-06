package com.liyakarimova.Net;

import com.liyakarimova.AuthCommands.AuthInformation;
import com.liyakarimova.commands.Command;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Net {

    public static Net instance;

    private SocketChannel channel;

    private Callback callback;

    private CallbackAuth callbackAuth;

    public static Net getInstance(Callback callback, CallbackAuth callbackAuth) {
        if (instance == null) {
            instance = new Net(callback, callbackAuth);
        }
        return instance;
    }

    private Net(Callback callback) {

        this.callback = callback;

        Thread thread = new Thread(() -> {
            EventLoopGroup worker = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();
            try {
                ChannelFuture channelFuture = bootstrap.group(worker)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                channel.pipeline().addLast(
                                        new ObjectEncoder(),
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new AuthClientHandler(callback),
                                        new ClientHandler(callback)
                                );
                            }
                        })
                        .bind(8189)
                        .sync();

                log.info("Client connected");
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });


        thread.setDaemon(true);
        thread.start();
}

public void sendCommand (Command cmd) throws IOException {
        channel.writeAndFlush(cmd);
}

public void sendAuth (AuthInformation authInformation) throws IOException {
    channel.writeAndFlush(authInformation);
}

}
