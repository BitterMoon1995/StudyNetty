package com.wstx.studynetty.section8;

import com.wstx.studynetty.section7.message.PingMessage;
import com.wstx.studynetty.section8.handler.TriggerH;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j
class Server {
    public static void main(String[] args) {
        NioEventLoopGroup g1 = new NioEventLoopGroup();
        NioEventLoopGroup g2 = new NioEventLoopGroup();
        NioEventLoopGroup g3 = new NioEventLoopGroup();

        new ServerBootstrap()
                .group(g1,g2)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("initChannel");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(
                                5,0,0));
                        pipeline.addLast(new TriggerH());
                        pipeline.addLast(new LoggingHandler());
                    }
                })
                .bind(9966);
    }
}
