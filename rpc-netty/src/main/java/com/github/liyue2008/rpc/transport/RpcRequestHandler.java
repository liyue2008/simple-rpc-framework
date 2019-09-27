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
package com.github.liyue2008.rpc.transport;

import com.github.liyue2008.rpc.client.ServiceTypes;
import com.github.liyue2008.rpc.client.stubs.RpcRequest;
import com.github.liyue2008.rpc.serialize.SerializeSupport;
import com.github.liyue2008.rpc.transport.command.Code;
import com.github.liyue2008.rpc.transport.command.Command;
import com.github.liyue2008.rpc.transport.command.Header;
import com.github.liyue2008.rpc.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author LiYue
 * Date: 2019/9/23
 */
public class RpcRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private List<Object> serviceProviders = new LinkedList<>();

    @Override
    public Command handle(Command requestCommand) {
        Header header = requestCommand.getHeader();
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());
        try {
            for (Object serviceProvider : serviceProviders) {
                for (Class<?> interfaceClass : serviceProvider.getClass().getInterfaces()) {
                    if (interfaceClass.getCanonicalName().equals(rpcRequest.getInterfaceName())) {
                        String arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                        Method method = interfaceClass.getMethod(rpcRequest.getMethodName(), String.class);
                        String result = (String ) method.invoke(serviceProvider, arg);
                        return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()), SerializeSupport.serialize(result));
                    }
                }
            }
            logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
        } catch (Throwable t) {
            logger.warn("Exception: ", t);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), t.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }
    @Override
    public void addServiceProvider(Object serviceProvider) {
        serviceProviders.add(serviceProvider);
    }
}
