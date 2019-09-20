package com.github.liyue2008.rpc.transport.netty;

import com.github.liyue2008.rpc.transport.InFlightRequests;
import com.github.liyue2008.rpc.transport.Transport;
import com.github.liyue2008.rpc.transport.command.Request;
import com.github.liyue2008.rpc.transport.command.Response;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class NettyTransport implements Transport, Closeable {
    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }




    @Override
    public  Future<Response> send(Request request) {
        CompletableFuture<Response> completableFuture = new CompletableFuture<>();
        try {
            inFlightRequests.put(new ResponseFuture(request, completableFuture));
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }


    @Override
    public void close()  {
        channel.close();
    }
}
