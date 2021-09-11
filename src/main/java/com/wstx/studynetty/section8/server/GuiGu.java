package com.wstx.studynetty.section8.server;

import com.wstx.studynetty.section8.handler.TriggerHd;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class GuiGu {
    public static void main(String[] args) {
        NioEventLoopGroup bossGp = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGp = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGp,workerGp)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pip = ch.pipeline();
                pip.addLast(new IdleStateHandler(4,0,0, TimeUnit.SECONDS));
                pip.addLast(new TriggerHd());
                pip.addLast(new LoggingHandler());
            }
        });
        bootstrap.bind(9966);

    }
}
