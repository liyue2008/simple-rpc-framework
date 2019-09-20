package com.github.liyue2008.rpc;

import java.io.Closeable;
import java.net.URI;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface RpcAccessPoint extends Closeable{
    <T> T getRemoteService(URI uri, Class<T> serviceClass);
    <T> URI addServiceProvider(T service);
    Closeable startServer();
}
