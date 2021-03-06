package com.wstx.studynetty.section1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class HelloNetty {
    public static void main(String[] args) {
        //启动器
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                //选择NioServerSocketChannel的实现，有nio、epoll(linux)、kQueue(mac)、bio
                .channel(NioServerSocketChannel.class)
                //为worker handler指定初始化器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    //Initializer通过initChannel初始化channel
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("服务器端连接建立后执行一次initChannel");
                        //pipeline这里译为“流水线”，是一个或多个handler（工序）的集合
                        //指定handlers（ChannelHandler... handlers）
                        //这里第一个handler为解码器，将netty接收到的byteBuffer转化为字符串，第二个为自写handler
                        ch.pipeline().addLast(
                                new IdleStateHandler(5,0,0),
                                new StringDecoder(),
                                new ServerHandler());
                    }
                })
                .bind(9966);
    }
}
