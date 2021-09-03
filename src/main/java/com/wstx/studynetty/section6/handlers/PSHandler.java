package com.wstx.studynetty.section6.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

//演示粘包与半包的处理器
//演示结果应为：服务器收到长度不等、个数不等的包，总共收到数据长度为saohua * 10
public class PSHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ByteBuf buffer = ctx.alloc().buffer();
            String saohua = "im a black big dick nigger";

            //换行符法。Windows与Linux不同
//            String saohua = "im a black big dick nigger\r\n";

            //自定义分隔符法：`
//            String saohua = "im a black big dick nigger`";

            //LFB解码器法
//            String head = "type:sync_frame";
            //写入固定32位的消息头，不足的用+号补满
//            buffer.writeBytes(genHead(head));
            //写入一个short表示长度，一个short占两个字节。
            buffer.writeShort(saohua.getBytes().length);

//            buffer.writeBytes(saohua.getBytes());

            ctx.writeAndFlush(saohua);
        }
    }

    @Test
    public void test() {
        byte[] niggers = genHead("nigger");
        System.out.println(Arrays.toString(niggers));
    }

    public static byte[] genHead(String contentStr){
        byte[] contentBytes = contentStr.getBytes();
        int len = contentBytes.length;
        StringBuilder builder = new StringBuilder();
        if (len == 32)
            return contentBytes;

        if (len > 32){
            for (int i = 0; i < 32; i++) {
                builder.append("e");
            }
        }
        else {
            int gap = 32 - len;
            builder.append(contentStr);
            for (int i = 0; i < gap; i++) {
                builder.append("+");
            }
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
