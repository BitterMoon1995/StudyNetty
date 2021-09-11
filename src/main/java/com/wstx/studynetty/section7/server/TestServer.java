package com.wstx.studynetty.section7.server;

import com.wstx.studynetty.section6.handlers.PSHandler;
import com.wstx.studynetty.section7.codec.MessageCodec;
import com.wstx.studynetty.section7.codec.SharableMsgCodec;
import com.wstx.studynetty.section7.message.LoginRequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

public class TestServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //LFB必须记录上一次操作的结果，特别在半包的情况下，所以本身是线程不安全的，也并没有@Sharable注解
        LengthFieldBasedFrameDecoder LFB = new LengthFieldBasedFrameDecoder(
                1024, 12,
                4, 0, 0);

        //日志处理器就是个打印功能不需要记录上一次的结果，是多线程安全的。
        //所以有@Sharable注解。应该单例使用，减小对象创建数量。
        final LoggingHandler loggingHandler = new LoggingHandler();

        //
        SharableMsgCodec sharableMsgCodec = new SharableMsgCodec();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(

                                new LengthFieldBasedFrameDecoder(
                                        1024,12,
                                        4,0,0),

                                //线程开到100时报如下错误：
                                //io.netty.channel.ChannelPipelineException: io.netty.handler.codec.
                                // LengthFieldBasedFrameDecoder is not a @Sharable handler,
                                // so can't be added or removed multiple times.
//                                LFB,
                                loggingHandler,
//                                new MessageCodec(),
                                sharableMsgCodec);
                    }
                })
                .bind(9966);
    }

}
