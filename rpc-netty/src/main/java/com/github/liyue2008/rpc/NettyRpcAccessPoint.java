/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.liyue2008.rpc;

import com.github.liyue2008.rpc.hello.HelloService;
import com.github.liyue2008.rpc.nameservice.LocalFileNameService;
import com.github.liyue2008.rpc.transport.RequestHandler;
import com.github.liyue2008.rpc.transport.RequestHandlerRegistry;
import com.github.liyue2008.rpc.transport.Transport;
import com.github.liyue2008.rpc.transport.netty.NettyClient;
import com.github.liyue2008.rpc.transport.netty.NettyServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {
    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private NettyServer server = null;
    private NettyClient client = new NettyClient();
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {

        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        if(HelloService.class.getCanonicalName().equals(serviceClass.getCanonicalName())) {
            return (T) new HelloServiceStub(transport);
        }

        throw new IllegalArgumentException(String.format("Unsupported service type: %s!", serviceClass.getCanonicalName()));
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()),30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        RequestHandler requestHandler = RequestHandlerRegistry.getInstance().get(serviceClass.getCanonicalName());

        if (null != requestHandler) {
            requestHandler.addServiceProvider(service);
            return uri;
        } else {
            throw new RuntimeException(String.format("No request handler of service :%s!", serviceClass.getCanonicalName()));
        }
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (null == server) {
            server = new NettyServer(RequestHandlerRegistry.getInstance(), port);
            server.start();

        }
        return () -> {
            if(null != server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() {
        if(null != server) {
            server.stop();
        }
        client.close();
    }
}
