package com.wstx.studynetty.section8.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

public class TriggerHd extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent == IdleStateEvent.READER_IDLE_STATE_EVENT){
                System.out.println("长时间未读");
                ctx.channel().close().sync().addListener(future -> {
                    System.out.println("已关闭");
                    /*
                    TODO:Redis维护两个SET，一个存在线无人机的SN一个存离线的，
                    TODO:这里就应该把SN拿到，然后在在线SN SET里面把它删除，再将其加入离线SN SET。
                    TODO:这样要请求所有在线无人机同步帧时，只需进行【两次】Redis请求：第一次请求online vehicleSn set，
                    TODO:第二次根据返回的在线无人机编号列表发multiGet请求，即可得到online vehicle syncFrame list。
                    TODO:请求离线无人机的，同理。
                     */
                    System.out.println("关闭后续逻辑......");
                });
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("channelRead");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
