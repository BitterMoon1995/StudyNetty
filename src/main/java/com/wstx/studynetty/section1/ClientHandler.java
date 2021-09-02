package com.wstx.studynetty.section1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 1; i++) {
            //发送消息到服务端
            ctx.writeAndFlush(Unpooled.copiedBuffer("nigger死妈了", CharsetUtil.UTF_8));
            Thread.sleep(500);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        if (msg instanceof ByteBuf) {
            System.out.println("收到ByteBuf");
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(byteBuf.toString());
            System.out.println(byteBuf.toString(Charset.defaultCharset()));
//            System.out.println("收到服务端" + ctx.channel().remoteAddress() + "的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        }

        else if (msg instanceof String)
            System.out.println("收到String");

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
