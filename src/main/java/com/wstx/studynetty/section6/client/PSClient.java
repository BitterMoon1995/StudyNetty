package com.wstx.studynetty.section6.client;

import com.wstx.studynetty.section6.handlers.PSHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

//演示粘包与半包的client
public class PSClient {
    @Test
    public void test() throws InterruptedException {
        NioEventLoopGroup bosses = new NioEventLoopGroup();
        NioEventLoopGroup workers = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(workers);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new PSHandler());
            }
        });
        bootstrap.connect("127.0.0.1",9966)
                .sync().channel().close().sync();
        workers.shutdownGracefully();

    }
}
