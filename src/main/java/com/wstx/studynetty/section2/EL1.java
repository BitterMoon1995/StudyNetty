package com.wstx.studynetty.section2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class EL1 {
    //创建事件循环组
    @Test
    public void test1() {
        //可执行io事件、普通任务、定时任务
        //可设置线程数，缺省情况下为NettyRuntime.availableProcessors() * 2
        EventLoopGroup group1 = new NioEventLoopGroup(2);
        //可执行普通任务、定时任务
        EventLoopGroup group2 = new DefaultEventLoopGroup();

        //next():获取下一个事件循环对象
        System.out.println(group1.next());
        System.out.println(group1.next());
        System.out.println(group1.next());
    }

    //事件循环执行普通任务
    public static void test2() {
        EventLoopGroup nioGroup = new NioEventLoopGroup();
        EventLoop eventLoop = nioGroup.next();
        Future<?> future = eventLoop.submit(() -> {
            System.out.println("nigga");
        });
        System.out.println("主线程");
    }

    //执行定时任务
    public static void test3() {
        EventLoopGroup nioGroup = new NioEventLoopGroup();
        EventLoop eventLoop1 = nioGroup.next();
        eventLoop1.scheduleAtFixedRate(() -> {
            System.out.println("固定频率任务执行");
        }, 0, 1, TimeUnit.SECONDS);
    }

    //执行IO事件
    @Test
    public static void test4() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                System.out.println(msg);
                                //可看到eventLoop与连接的“负责到底、多路复用”的关系
                                EventLoop eventLoop = ctx.channel().eventLoop();
                                System.out.println(eventLoop);
                                ByteBuf buf = (ByteBuf) msg;
                                //生产环境下务必指定字符集
                                String buf_str = buf.toString(StandardCharsets.UTF_8);
                                System.out.println(buf_str);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            }
                        });
                    }
                })
                .bind(9966);
    }

    public static void main(String[] args) {
        test4();
    }
}
