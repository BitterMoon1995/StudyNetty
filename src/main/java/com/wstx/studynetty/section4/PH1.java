package com.wstx.studynetty.section4;

import com.wstx.studynetty.section4.handlers.InH1;
import com.wstx.studynetty.section4.handlers.InH2;
import com.wstx.studynetty.section4.handlers.InH3;
import com.wstx.studynetty.section4.handlers.OutH1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class PH1 {
    public void seg1(){
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new InH1(),new InH2(),new InH3(),new OutH1());
                    }
                })
                .bind(9966);
    }

    public static void main(String[] args) {
        PH1 ph1 = new PH1();
        ph1.seg1();
    }
}
