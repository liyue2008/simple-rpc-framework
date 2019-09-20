package com.github.liyue2008.rpc.server;

import com.github.liyue2008.rpc.hello.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(String name) {
        logger.info("收到请求: {}.", name);
        return "Hello, " + name + "!";
    }
}
