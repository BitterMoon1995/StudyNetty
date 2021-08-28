package com.wstx.studynetty.section1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//handler分为inbound与outbound，inbound管入站，数据进入；outbound管出站，数据往外写。
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
    }

    //指定处理读事件的逻辑
    //建立连接（服务器端accept）不会触发，收到消息（read事件）才会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + msg);
    }

    //如果客户端是发送信息的短连接，则此方法会触发两次
    //第一次为accept事件触发，第二次是read事件触发
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
//        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息，并给你发送一个问号?", CharsetUtil.UTF_8));
        super.channelReadComplete(ctx);
    }

    //客户端主动关闭连接后会抛远程主机关闭，嫌烦可重写exceptionCaught
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
//        super.exceptionCaught(ctx, cause);
    }
}
