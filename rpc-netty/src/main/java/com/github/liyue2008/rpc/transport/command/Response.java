package com.github.liyue2008.rpc.transport.command;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class Response<T> extends Command<T> {
    public Response(ResponseHeader header, T payload) {
        super(header, payload);
    }

    public Response(Throwable t){
        super(null, null);
    }

    @Override
    public String toString() {
        // TODO
        return super.toString();
    }
}
