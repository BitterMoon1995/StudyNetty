package com.wstx.studynetty.section7.server;

import com.wstx.studynetty.section6.handlers.PSHandler;
import com.wstx.studynetty.section7.codec.MessageCodec;
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

//        LengthFieldBasedFrameDecoder LFB = new LengthFieldBasedFrameDecoder(
//                1024, 12,
//                4, 0, 0);

        //日志处理器有Sharable注解，是多线程安全的，可以单例使用，减小对象创建数量
        final LoggingHandler loggingHandler = new LoggingHandler();

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

                                //非@sharable decoder，线程不安全，不能单例使用。线程开到100时报如下错误：
                                //io.netty.channel.ChannelPipelineException: io.netty.handler.codec.
                                // LengthFieldBasedFrameDecoder is not a @Sharable handler,
                                // so can't be added or removed multiple times.
//                                LFB,
                                loggingHandler,
                                new MessageCodec());
                    }
                })
                .bind(9966);
    }
    @Test
    public void clientTest() throws InterruptedException {
        NioEventLoopGroup workers = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(workers);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        LoginRequestMessage msg =
                                new LoginRequestMessage("nigger_slave", "123456");
                        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
                        new MessageCodec().iEncode(null,msg,buf);

                        //粘包、半包测试
                        for (int i = 0; i < 100; i++) {
                            buf.retain();
                            ctx.writeAndFlush(buf);
                        }
                    }
                });
            }
        });
        bootstrap.connect("127.0.0.1",9966)
                .sync().channel().close().sync();
        workers.shutdownGracefully();
    }
}
