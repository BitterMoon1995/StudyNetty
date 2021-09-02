package com.wstx.studynetty.section5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

@Slf4j
public class BF1 {
    //初始化、write
    @Test
    public void test() {
        //默认256容量，可以动态扩容
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf byteBuf2 = ByteBufAllocator.DEFAULT.buffer(1024);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            builder.append("a");
        }
        log.debug(String.valueOf(byteBuf));
        //writeXXX会改变写指针
        byteBuf.writeBytes(builder.toString().getBytes());
        log.debug(String.valueOf(byteBuf));
    }

    //read
    @Test
    public void test2() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }
        //readXXX会改变读指针
        byteBuf.readByte();
        log.debug(byteBuf.toString());
        ByteBuf dstBf = ByteBufAllocator.DEFAULT.buffer(5);
        byteBuf.readBytes(dstBf);
        log.debug(byteBuf.toString());
    }

    //get & set
    //getXXX setXXX都不会改变读、写指针
    @Test
    public void test3() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        byteBuf.setByte(4,4397);
        log.debug(byteBuf.toString());

        byteBuf.getByte(0);
        log.debug(byteBuf.toString());
    }

    //冷知识：ByteBuf的toString()与toString(xxxCharset)有极带的区别
    //前者得到该BF的元信息，后者才能得到解码后的可读内容
    @Test
    public void test4() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes("我是尼哥黑人".getBytes());
        System.out.println(byteBuf.toString());
        System.out.println(byteBuf.toString(Charset.defaultCharset()));
    }

    //热知识：ByteBuf使用的是【直接内存】，并采用【池化】技术
    @Test
    public void test5() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        //class io.netty.buffer.[Pooled]Unsafe[Direct]ByteBuf
        System.out.println(byteBuf.getClass());
    }

    /*
    特别注意：在ByteBuf使用完毕后务必将其释放，否则内存泄露。
    一般在pipeline中最后一个handler里将其释放。

    可以使用retain()延缓其释放,并使计数器+1，而release()会将其计数器-1，
    当计数器为0时，则会被真正释放。

    head 和tail
     */
    @Test
    public void test6() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        byteBuf.retain();

        for (int i = 0; i < 10000; i++) {
            byteBuf.writeByte(i);
        }
        byteBuf.readByte();
        log.debug(byteBuf.toString());

        byteBuf.release();
        byteBuf.release();

        //PooledUnsafeDirectByteBuf(freed)
        log.debug(byteBuf.toString());
    }
}
