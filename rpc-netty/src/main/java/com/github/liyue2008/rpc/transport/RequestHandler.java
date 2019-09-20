package com.github.liyue2008.rpc.transport;

import com.github.liyue2008.rpc.transport.command.Response;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface RequestHandler<R/*Request Type*/,P/*Response Type*/> {
    Response<P> handle(R command, Transport transport);
}
