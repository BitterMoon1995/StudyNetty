package com.wstx.studynetty.section7.codec;

import com.wstx.studynetty.section7.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 上一个handler必须为LFB,确保ByteBuf in是完整的
 */
@Slf4j
@ChannelHandler.Sharable
public class SharableMsgCodec extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(new byte[]{'w','s','t','x'});
        //2个字节的版本号
        buf.writeShort(64);
        //1个字节的序列化方式 protobuf-0 json-1 jdk-2
        buf.writeByte(2);
        //1个字节的指令类型
        buf.writeByte(msg.getMessageType());
        //4个字节的请求序号（双工通信要用）
        buf.writeInt(msg.getSequenceId());

        //先获取消息内容的字节数组
        //暂用JDK序列化协议试水。该协议存在问题：无法跨平台；消息包含java全类名，浪费...啥都浪费
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        ObjectOutputStream oOs = new ObjectOutputStream(baOs);
        oOs.writeObject(msg);
        byte[] msgByteArr = baOs.toByteArray();

        //再填入4个字节的长度
        buf.writeInt(msgByteArr.length);
        //写入内容
        buf.writeBytes(msgByteArr);

        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        short version = in.readShort();
        byte serializerWay = in.readByte();
        byte insType = in.readByte();
        int sequenceId = in.readInt();
        int length = in.readInt();

        //准备缓冲区字节数组
        byte[] buffer = new byte[length];
        in.readBytes(buffer,0,length);

        if (serializerWay == 2){
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(new ByteArrayInputStream(buffer));
            Message message = (Message) objectInputStream.readObject();
            out.add(message);
            log.debug("{}",message);
        }
        log.debug("{},{},{},{},{},{}",magicNum,version,serializerWay,insType,sequenceId,length);
    }
}
