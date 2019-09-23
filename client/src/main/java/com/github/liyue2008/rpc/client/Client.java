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
package com.github.liyue2008.rpc.client;

import com.github.liyue2008.rpc.NameService;
import com.github.liyue2008.rpc.RpcAccessPoint;
import com.github.liyue2008.rpc.hello.HelloService;
import com.github.liyue2008.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String [] args) throws IOException {
        String serviceName = HelloService.class.getCanonicalName();
        NameService nameService = ServiceSupport.load(NameService.class);
        URI uri = nameService.lookupService(serviceName);
        String name = "Master MQ";
        if(null != uri) {
            logger.info("找到服务{}，提供者: {}.", serviceName, uri);
            try(RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
                HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
                logger.info("请求服务, name: {}...", name);
                String response = helloService.hello(name);
                logger.info("收到响应: {}.", response);
            }
        } else {
            logger.info("找不到服务提供者: {}!", serviceName);
        }

    }
}
