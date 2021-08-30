package com.wstx.studynetty.section3;

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
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

//netty的网络连接建立与关闭都是异步调用，不能直接用同步的代码逻辑来处理
//连接建立，需要借助channelFuture；关闭，需要借助closeFuture。
@Slf4j
public class As1 {
    //同步处理channel的连接
    public void segment1() throws InterruptedException {
        Bootstrap bootstrap = getBootstrap(null);

        //future、promise都是和异步方法配套使用，用于处理异步调用的结果
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9966);
        //主线程对connect方法的调用是异步非阻塞的。会直接交给另一个nio线程执行，并不关心调用的结果。
        //如果不调用sync()方法，主线程会立即向下执行channel()方法，但是此时连接并来不及建立，获取的通道是无效的。
        //sync将阻塞调用异步方法的线程，直到连接建立。
        channelFuture.sync();

        Channel channel = channelFuture.channel();
        System.out.println(channel);
        channel.writeAndFlush("nigger bees");
    }

    //addListener异步处理channel的连接结果
    public void seg2() {
        Bootstrap bootstrap = getBootstrap(null);

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9966);
        Channel channel = channelFuture.channel();
        System.out.println("*********************************");
        System.out.println(channel);
        channelFuture.addListener((ChannelFutureListener) future -> {
            System.out.println("-------------------------------------");
            System.out.println(future.channel());
            future.channel().writeAndFlush("nigga");
        });
    }

    //正确处理channel关闭
    public void seg3() throws InterruptedException {
        Bootstrap bootstrap = getBootstrap(null);
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9966);
        Channel channel = channelFuture.sync().channel();

        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                channel.writeAndFlush(line);
                if (line.equals("q")) {
                    //close也是异步调用
                    channel.close();
                    //如果要在连接关闭后执行一些操作，不能简单在close方法后执行，因为执行后续操作的线程（这里为user_input线程）
                    //和close的线程（负责这个channel的eventLoop线程）不是一个，也无法控制先后。
//                    log.debug("连接关闭后的操作......");
                    break;
                }
            }
        }, "user_input");
        thread.start();

        //同步处理连接关闭。这里log.debug写在主线程里所以是主线程来执行
//        ChannelFuture closeFuture = channel.closeFuture();
//        closeFuture.sync();
//        log.debug("同步处理连接关闭后的操作......");

        //异步处理连接关闭。这里由从头到尾负责这个channel的事件循环来执行
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(future -> {
            log.debug("异步处理连接关闭后的操作......");
        });
    }

    public static void main(String[] args) throws InterruptedException {
        As1 as1 = new As1();
        as1.seg3();
    }

    public static Bootstrap getBootstrap(NioEventLoopGroup group) {
        if (group == null)
            group = new NioEventLoopGroup();
        return new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG),new StringEncoder());
                    }
                });
    }
}
