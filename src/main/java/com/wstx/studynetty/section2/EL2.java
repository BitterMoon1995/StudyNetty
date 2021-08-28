package com.wstx.studynetty.section2;

import com.wstx.studynetty.section2.handlers.H0;
import com.wstx.studynetty.section2.handlers.H1;
import com.wstx.studynetty.section2.handlers.H2;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class EL2 {
    public static void test1() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(3);
        DefaultEventLoopGroup dbAccessGroup = new DefaultEventLoopGroup(5);
        //细分一：改为双group，前一个为boss group，处理accept事件；后一个为worker group，处理read&write事件
        //如果只传一个group(未改的情况下)，则workerGroup又当parentGroup又当childGroup
        //看不太到优化后的效果
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
//                                .addLast(new H0());
                                //细分二：耗时长的复杂任务交给第三个或更多个group去处理。
                                //本例中，会发现workerGroup的handler总能第一时间返回，
                                //而不会受到dbAccessGroup的handler的SQL延迟的影响
                                .addLast(workerGroup, new H1())
                                .addLast(dbAccessGroup, new H2());
                    }
                })
                .bind(9966);
    }

//    源码阅读之：fireChannelRead后发生了什么？
//    AbstractChannelHandlerContext.java ：
//    static void invokeChannelRead(final AbstractChannelHandlerContext next, Object msg) {
//        final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, "msg"), next);
//        EventExecutor executor = next.executor();
//    如果当前线程属于下一个循环组
//        if (executor.inEventLoop()) {
//            next.invokeChannelRead(m);
//    不属于
//        } else {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    next.invokeChannelRead(m);
//                }
//            });
//        }
//    }

    public static void main(String[] args) {
        test1();
    }
}
