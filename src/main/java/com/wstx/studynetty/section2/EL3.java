package com.wstx.studynetty.section2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EL3 {
    //优雅关闭EventLoopGroup
    //逻辑为连接——发nigga——关闭channel——关闭事件循环组
    public void seg(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG), new StringEncoder());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9966);
        Channel channel = channelFuture.channel();

        channelFuture.addListener((ChannelFutureListener)future -> {
            channel.writeAndFlush("nigga");
            channel.close();
        });
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(future -> {
            group.shutdownGracefully();
        });
    }

    public static void main(String[] args) {
        EL3 el3 = new EL3();
        el3.seg();
    }
}
