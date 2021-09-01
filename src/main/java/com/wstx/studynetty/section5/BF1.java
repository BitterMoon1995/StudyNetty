package com.wstx.studynetty.section5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class BF1 {
    //初始化、写入
    @Test
    public void test() {
        //默认256容量，可以动态扩容
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf byteBuf2 = ByteBufAllocator.DEFAULT.buffer(1024);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            builder.append("a");
        }
        byteBuf.writeBytes(builder.toString().getBytes());
        log.debug(String.valueOf(byteBuf));
        //getXXX不改变读指针
        byteBuf.getByte(0);
//        System.out.println(byte2);
        log.debug(String.valueOf(byteBuf));
        //readXXX会改变读指针
        byteBuf.readByte();
        ByteBuf dstBf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.readBytes(dstBf);
        log.debug(String.valueOf(byteBuf));

    }

    //热知识：ByteBuf使用的是【直接内存】，并采用【池化】技术
    @Test
    public void test2() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        //class io.netty.buffer.[Pooled]Unsafe[Direct]ByteBuf
        System.out.println(byteBuf.getClass());
    }

    //Netty-ByteBuf零拷贝的：切片
    @Test
    public void test3() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.writeBytes("abcdefghij".getBytes());
        byte[] bytes = "abcdefghij".getBytes();
        log.debug(String.valueOf(byteBuf));
    }
}
