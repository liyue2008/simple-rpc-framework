package com.github.liyue2008.rpc;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {
    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        return null;
    }

    @Override
    public <T> URI addServiceProvider(T service) {
        return null;
    }

    @Override
    public Closeable startServer() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
