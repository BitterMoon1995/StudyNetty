package com.wstx.studynetty.phase1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.Test;

import java.net.InetSocketAddress;

public class HelloClient {
    @Test
    public void ali() throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            System.out.println("客户端准备就绪，随时可以起飞~");
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9966).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }

    @Test
    public void heima() throws InterruptedException {
        //配置
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    //在连接建立后被调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("建立连接后调用一次initChannel");
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });
        //连接
        Channel channel = bootstrap
                .connect(new InetSocketAddress("127.0.0.1", 9966))
                //同步（阻塞），直到.connect才.channel。直到连接建立，才获取channel
                .sync()
                //代表建立连接后，客户端与服务器之间的socket channel
                .channel();
        //发消息
        channel
                .writeAndFlush("我是黑人")
                //不阻塞，发完就return。可设置延迟
                .await();
    }
}
