package com.wstx.studynetty.section2.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class H1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("---handler1 channelRead---");
//        System.out.println(ctx.channel().eventLoop());
        //将msg传递给同一pipeline中下一个handler的channelRead方法
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
