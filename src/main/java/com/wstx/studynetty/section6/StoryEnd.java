package com.wstx.studynetty.section6;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import sun.nio.cs.UTF_32;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/*
泪目最终章之netty接收其他语言传来的JSON数据，同时完美处理粘包，最终调用worker group解析、同步、持久化
 */

public class StoryEnd {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                //这个亲爹得在最前面
                                new StringEncoder(),
                                //实测已生效
                                new FixedLengthFrameDecoder(237),
                                new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                if (msg instanceof ByteBuf){
                                    System.out.println("read bf");
                                    System.out.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
                                }
                                if (msg instanceof String){
                                    System.out.println("read str");
                                    System.out.println(msg);
                                }
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("channelReadComplete");
                            }
                        });
                    }
                })
                .bind(9966);
    }

    @Test
    public void analyzeBFStructure() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        String saohua = "im a black big dick nigger";
        byteBuf.writeShort(saohua.getBytes().length);
        byteBuf.writeBytes(saohua.getBytes());
        ByteBuf res = byteBuf.readBytes(2);
        System.out.println(res.toString(StandardCharsets.UTF_8));

    }

    @Test
    public void normal() throws InterruptedException {
        NioEventLoopGroup workers = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(workers)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                                ctx.writeAndFlush(Unpooled.copiedBuffer("nigger死妈了", CharsetUtil.UTF_8));
                                ctx.writeAndFlush("nigga");
                            }
                        });
                    }
                });

        bootstrap.connect("127.0.0.1",9966)
                .sync().channel().close().sync();
        workers.shutdownGracefully();
    }
}
