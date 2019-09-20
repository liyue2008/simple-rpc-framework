package com.github.liyue2008.rpc.transport.command;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class Request<T> extends Command<T> {
    public Request(RequestHeader header, T payload) {
        super(header, payload);
    }
}
