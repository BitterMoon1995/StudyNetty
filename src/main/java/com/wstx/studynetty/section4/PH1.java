package com.wstx.studynetty.section4;

import com.wstx.studynetty.section1.ServerHandler;
import com.wstx.studynetty.section4.handlers.InH1;
import com.wstx.studynetty.section4.handlers.InH2;
import com.wstx.studynetty.section4.handlers.InH3;
import com.wstx.studynetty.section4.handlers.OutH1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PH1 {
    public void seg1(){
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        //入、出站处理器之间的结构为双向链表
                        ch.pipeline().addLast(new StringDecoder(),
                                new InH1(),new InH2(),new InH3(),
                                new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void read(ChannelHandlerContext ctx) throws Exception {
                                        log.debug("出站处理器 1 号 read");
                                        super.read(ctx);
                                    }
                                },
                                new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void read(ChannelHandlerContext ctx) throws Exception {
                                        log.debug("出站处理器 2 号 read");
                                        super.read(ctx);
                                    }
                                },
                                new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void read(ChannelHandlerContext ctx) throws Exception {
                                        log.debug("出站处理器 3 号 read");
                                        super.read(ctx);
                                    }
                                });
                    }
                })
                .bind(9966);
    }

    public static void main(String[] args) {
        PH1 i = new PH1();
        i.seg1();
    }
}
