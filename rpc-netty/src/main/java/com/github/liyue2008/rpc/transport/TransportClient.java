package com.github.liyue2008.rpc.transport;

import com.github.liyue2008.rpc.transport.Transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * @author LiYue
 * Date: 2019/9/25
 */
public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException;

    @Override
    void close();
}
