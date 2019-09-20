package com.github.liyue2008.rpc.transport.netty;

import com.github.liyue2008.rpc.transport.command.Request;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {

    }
}
