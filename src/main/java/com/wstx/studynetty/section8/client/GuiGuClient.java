package com.wstx.studynetty.section8.client;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 普通血泪教训：
 * IdleStateHandler存在的意义是长连接保活，不处理短连接，不处理连接断开。
 * 这里逻辑很简单：
 * 1. server设置4秒读空闲
 * 2. client建立连接，发包，阻塞15秒（同时连接维持）
 * 3. 4秒后读空闲时间到，IdleStateHandler 触发channelIdle；8秒时再触发；12秒时再触发；共触发3次。
 * 4. 15秒后，连接断开，【但是关IdleStateHandler屁事】，人家不处理连接断开
 * 之前问题：
 * 写的client全是错的，不管是发1次就断，还是发10次每次间隔1秒，都根本不会触发channelIdle
 */
public class GuiGuClient {
    @Test
    public void test() throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1",9966);
        String s = "I love black dick";
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(s.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        Thread.sleep(1000 * 15);
        socket.close();
    }

    @Test
    public void morePractical() throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1",9966);
        byte[] bytes = "I love black dick".getBytes(StandardCharsets.UTF_8);
        OutputStream oStream = socket.getOutputStream();

        for (int i = 0; i < 5; i++) {
            oStream.write(bytes);
            oStream.flush();
            Thread.sleep(1000);
        }

        Thread.sleep(1000 * 15);
        //实际情况无人机是不会主动关闭连接的，大概

        //供参考：
        while (true){
            if (!socket.getKeepAlive())
                break;
        }
    }
}
