package com.wstx.studynetty.section5;

import com.wstx.studynetty.section1.ServerHandler;
import com.wstx.studynetty.section2.handlers.H1;
import com.wstx.studynetty.section2.handlers.H2;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class BF3 {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder(),new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                //使用ctx.alloc().buffer拿字节缓冲
                                ByteBuf respBf = ctx.alloc().buffer(20);
                                System.out.println(respBf);
                                respBf.writeBytes("server received:".getBytes());
                                if (msg instanceof String)
                                    respBf.writeBytes(((String) msg).getBytes());
                                //注意这里发回去的是ByteBuf，其他语言担怕用不了
                                ctx.writeAndFlush(respBf);
                                //这里释放炸了，为什么？
//                                respBf.release();
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

                            }
                        });
                    }
                })
                .bind(9966);

    }
}
