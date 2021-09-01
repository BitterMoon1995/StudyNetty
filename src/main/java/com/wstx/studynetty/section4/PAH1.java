package com.wstx.studynetty.section4;

import com.wstx.studynetty.section4.handlers.InH1;
import com.wstx.studynetty.section4.handlers.InH2;
import com.wstx.studynetty.section4.handlers.InH3;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PAH1 {
    public void seg1(){
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        /*
                        入、出站处理器之间的结构为双向链表。
                        此处情景为head->InH1->InH2->InH3->OutH3->OutH2->OutH1->tail。
                        此处handler编排逻辑为：InH1、InH2对msg进行处理后，一律调用ctx.fireChannelRead
                        该方法将处理后的消息传递给从head开始从前往后数下一个InH，最后到InH3，
                        InH3不再调用该方法因为后面已经没有InH了，
                        而是在处理消息后调用ctx.writeAndFlush，该方法会从tail开始从后往前找
                        第一个OutH，而后每一个OutH都应该调用ctx.write，将消息传递给从后往前下一个OutH
                        */
                        ch.pipeline().addLast(new StringDecoder(),
                                new InH1(),new InH2(),
                                new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.debug("出站处理器 3 号 write");
                                        super.write(ctx, msg, promise);
                                    }
                                },
                                new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.debug("出站处理器 2 号 write");
                                        super.write(ctx, msg, promise);
                                    }
                                },
                                new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.debug("出站处理器 1 号 write");
                                        super.write(ctx, msg, promise);
                                    }
                                },
                                new InH3());
                    }
                })
                .bind(9966);
    }

    public static void main(String[] args) {
        PAH1 i = new PAH1();
        i.seg1();
    }
}
