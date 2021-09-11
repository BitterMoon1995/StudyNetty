package com.wstx.studynetty.section6.server;

import com.wstx.studynetty.section1.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

//打印日志的服务器
public class LogServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        new ServerBootstrap()
                /*
                演示半包现象所需增加设置的选项：把传输层的滑动窗口调小，故意小于想要发的包大小
                 */
//                .option(ChannelOption.SO_RCVBUF,10)
                .group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                /*
                还可以调整应用层接收缓冲大小
                 */
//                .childOption(ChannelOption.RCVBUF_ALLOCATOR,
//                        new AdaptiveRecvByteBufAllocator(16,16,16))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
//                        ByteBuf diyDelimiter = ByteBufAllocator.DEFAULT.buffer(4);
//                        diyDelimiter.writeBytes("`".getBytes());

                        ch.pipeline().addLast(
                                //粘包解决方案一：全部改成短连接，这个不聊了

                                //粘包解决方案二：添加定长解码器
//                                new FixedLengthFrameDecoder(26),

                                //粘包解决方案三：添加换行解码器，根据换行符的存在界定消息边界
//                                new LineBasedFrameDecoder(512),
                                //也可以自定义分隔符
                                //分隔符不会被算作数据包的内容
                                //缺陷在于效率较低，要逐字节找分隔符
//                                new DelimiterBasedFrameDecoder(512,diyDelimiter),

                                //粘包解决方案四：LFB解码器
                                //个人理解：length字段从第几个字节开始(防消息头)？length字段本身几个字节？
                                //length字段之后又有几个字节不属于实际内容(防第二个消息头)？
                                //不管length，反正总共要剥离头几个字节？

                                //32位的消息头，2位的长度字段，没有第二个消息头，
                                //一个字符都不砍（消息头、长度字段、实际内容都看）
                                new LengthFieldBasedFrameDecoder
                                        (1024,
                                                32,
                                                4,
                                                0,
                                                0),
                                new LoggingHandler(LogLevel.DEBUG));
                    }
                })
                .bind(9966);
    }
}
