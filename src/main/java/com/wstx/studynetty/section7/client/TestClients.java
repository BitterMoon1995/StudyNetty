package com.wstx.studynetty.section7.client;

import com.wstx.studynetty.section7.codec.MessageCodec;
import com.wstx.studynetty.section7.message.LoginRequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

public class TestClients {
    @Test
    public void jdkEncodedMsg() throws InterruptedException {
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
                        for (int i = 0; i < 1; i++) {
                            ch.writeAndFlush(buf);
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
