package com.github.liyue2008.rpc.transport.command;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class Command<T> {
    protected Header header;
    protected T payload;

    public Command(Header header, T payload) {
        this.header = header;
        this.payload = payload;
    }
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
