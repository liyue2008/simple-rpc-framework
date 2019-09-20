package com.github.liyue2008.rpc.transport.netty;

import com.github.liyue2008.rpc.transport.command.Request;
import com.github.liyue2008.rpc.transport.command.Response;

import java.util.concurrent.CompletableFuture;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class ResponseFuture {
    private final Request request;
    private final CompletableFuture<Response> future;
    private final long timestamp;

    public ResponseFuture(Request request, CompletableFuture<Response> future) {
        this.request = request;
        this.future = future;
        timestamp = System.nanoTime();
    }

    public Request getRequest() {
        return request;
    }

    public CompletableFuture<Response> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
