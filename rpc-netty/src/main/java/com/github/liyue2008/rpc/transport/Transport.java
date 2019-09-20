package com.github.liyue2008.rpc.transport;

import com.github.liyue2008.rpc.transport.command.Request;
import com.github.liyue2008.rpc.transport.command.Response;

import java.util.concurrent.Future;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface Transport {
    Future<Response> send(Request request);
}
