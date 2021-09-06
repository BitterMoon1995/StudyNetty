package com.wstx.studynetty.section7.server;

import com.wstx.studynetty.section7.codec.MessageCodec;
import com.wstx.studynetty.section7.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

public class TestChannel {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                //配合LFB解决粘包、半包问题
                //initialBytesToStrip必须设为0

//                【待测。写个server。】

                new LengthFieldBasedFrameDecoder(
                        1024,12,
                        4,0,0),
                new LoggingHandler(),
                new MessageCodec());
        LoginRequestMessage msg = new LoginRequestMessage("nigger_slave", "123456");

        //编码测试
//        channel.writeOutbound(msg);

        //解码测试
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().iEncode(null,msg,buf);
//        channel.writeInbound(buf);

        //粘包、半包测试
        for (int i = 0; i < 10; i++) {
            buf.retain();
            channel.writeInbound(buf);
        }

    }
}
