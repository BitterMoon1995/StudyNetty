package com.wstx.studynetty.section5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

//BYTE BUF对”零拷贝“的实现
@Slf4j
public class BF2 {
    //Netty-ByteBuf零拷贝的：切片
    //切片的初始化不发生内存复制，而是使用原始BF的内存对象；关键在于切片会维护独立的读、写指针
    @Test
    public void test1() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.writeBytes("abcdefghij".getBytes());
        ByteBuf slice = byteBuf.slice(0, 5);
        slice.setByte(0,'z');
        System.out.println(byteBuf.getByte(0));
    }

    @Test
    public void test2() {
        ByteBuf srcBuf1 = ByteBufAllocator.DEFAULT.buffer();
        srcBuf1.writeBytes("abcde".getBytes());
        ByteBuf srcBuf2 = ByteBufAllocator.DEFAULT.buffer();
        srcBuf2.writeBytes("ghijk".getBytes());

        //重新开辟了内存空间，属实不行
//        ByteBuf dstBuf = ByteBufAllocator.DEFAULT.buffer();
//        dstBuf.writeBytes(srcBuf1).writeBytes(srcBuf2);

        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeByteBuf.addComponents(true,srcBuf1,srcBuf2);
        log.debug(compositeByteBuf.toString());
        //验证确实还是和srcBuf1、srcBuf2共享堆对象
        compositeByteBuf.setByte(1,-110);
        System.out.println(srcBuf1.getByte(1));
    }
}
