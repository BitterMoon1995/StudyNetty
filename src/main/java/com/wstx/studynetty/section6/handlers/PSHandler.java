package com.wstx.studynetty.section6.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//演示粘包与半包的处理器
//演示结果应为：服务器收到长度不等、个数不等的包，总共收到数据长度为saohua * 10
public class PSHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ByteBuf buffer = ctx.alloc().buffer();
            String saohua = "im black big dick nigger";
            buffer.writeBytes(saohua.getBytes());
            ctx.writeAndFlush(buffer);
        }
    }
}
