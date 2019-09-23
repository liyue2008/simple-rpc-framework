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
import com.github.liyue2008.rpc.serialize.SerializeSupport;
import com.github.liyue2008.rpc.transport.Transport;
import com.github.liyue2008.rpc.transport.command.Code;
import com.github.liyue2008.rpc.transport.command.Command;
import com.github.liyue2008.rpc.transport.command.RequestHeader;
import com.github.liyue2008.rpc.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author LiYue
 * Date: 2019/9/23
 */
public class HelloServiceStub implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceStub.class);
    private final Transport transport;

    HelloServiceStub(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String hello(String name) {
        RequestHeader header = new RequestHeader(ServiceTypes.TYPE_HELLO_SERVICE, 1, RequestIdSupport.next());
        byte [] payload = SerializeSupport.serialize(name);
        Command requestCommand = new Command(header, payload);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if(responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return SerializeSupport.parse(responseCommand.getPayload());
            } else {
                throw new Exception(responseHeader.getError());
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
