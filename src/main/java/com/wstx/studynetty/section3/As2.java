package com.wstx.studynetty.section3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

//netty中这么多异步和多线程，那就得聊聊线程间通信的含金量了
//jdk future、netty future、netty promise本质都是线程间通信的容器
//但是只有promise才有go channel的味道
@Slf4j
public class As2 {
    //jdk`s future
    public void seg1(){
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(2000);
                log.debug("任务执行完毕");
//                System.out.println(1/0);
                return 4396;
            }
        });

        try {
            //future的get方法会阻塞调用者线程（aka主线程），等待任务执行结束，然后获取结果
            //jdk future缺点：必须同步；必须等待执行结束；不能手动填入结果
            Integer res = future.get();
            log.debug("结果为："+res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    //netty future
    public void seg2(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        io.netty.util.concurrent.Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(2000);
                log.debug("任务执行完毕");
//                System.out.println(1/0);
                return 4396;
            }
        });

        //netty future - 同步
//        try {
//            Integer res = future.get();
//            log.debug("结果为："+res);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        } finally {
//            group.shutdownGracefully();
//        }

        future.addListener(future1 -> {
            //可以确定已经有结果了，所以getNow()。
            //netty future缺点：能异步了，不用等执行结束；但是还是不能在执行方手动填入数据
            Object res = future1.getNow();
            log.debug("结果为：{}", res);
            group.shutdownGracefully();
        });
    }

    //netty promise
    //最大的意义是实现任务执行方与调用方的双工通信
    public void seg3(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                int a = 1/0;
                promise.setSuccess(4396);
            } catch (Exception e) {
                promise.setFailure(e);
            }
            log.debug("任务执行完毕");
        }).start();

        try {
            log.debug("开始等待结果");
            Integer res = promise.get();
            log.debug("结果为：{}", res);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("这下");
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        As2 ayaka = new As2();
        ayaka.seg3();
    }
}
