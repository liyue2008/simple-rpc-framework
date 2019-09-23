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

import com.github.liyue2008.rpc.ServiceTypes;
import com.github.liyue2008.rpc.hello.HelloService;
import com.github.liyue2008.rpc.serialize.SerializeSupport;
import com.github.liyue2008.rpc.transport.command.Command;
import com.github.liyue2008.rpc.transport.command.Header;
import com.github.liyue2008.rpc.transport.command.ResponseHeader;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author LiYue
 * Date: 2019/9/23
 */
public class HelloRequestHandler implements RequestHandler {
    private List<HelloService> serviceProviders = new LinkedList<>();

    @Override
    public Command handle(Command requestCommand) {
        String name = SerializeSupport.parse(requestCommand.getPayload());
        Header header = requestCommand.getHeader();
        if(!serviceProviders.isEmpty()) {
            HelloService serviceProvider = serviceProviders.get(ThreadLocalRandom.current().nextInt(serviceProviders.size()));
            String ret = serviceProvider.hello(name);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()), SerializeSupport.serialize(ret));
        } else {
            throw new RuntimeException("No service Provider of HelloService!");
        }

    }

    @Override
    public String className() {
        return HelloService.class.getCanonicalName();
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_HELLO_SERVICE;
    }
    @Override
    public void addServiceProvider(Object serviceProvider) {
        if(serviceProvider instanceof HelloService) {
            serviceProviders.add((HelloService )serviceProvider);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
