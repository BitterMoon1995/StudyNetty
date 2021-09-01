package com.wstx.studynetty.section4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PAH2 {
    ChannelInboundHandlerAdapter inHandler1 = new ChannelInboundHandlerAdapter(){
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.debug("1号inHandler Read");
            super.channelRead(ctx, msg);
        }
    };
    ChannelInboundHandlerAdapter inHandler2 = new ChannelInboundHandlerAdapter(){
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.debug("2号inHandler Read");
            super.channelRead(ctx, msg);
        }
    };
    ChannelOutboundHandlerAdapter outHandler1 = new ChannelOutboundHandlerAdapter(){
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.debug("1号outHandler write");
            super.write(ctx, msg, promise);
        }
    };
    ChannelOutboundHandlerAdapter outHandler2 = new ChannelOutboundHandlerAdapter(){
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.debug("2号outHandler write");
            super.write(ctx, msg, promise);
        }
    };
    EmbeddedChannel embeddedChannel = new EmbeddedChannel(inHandler1,inHandler2,outHandler1,outHandler2);
}
