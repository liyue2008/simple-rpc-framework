package com.github.liyue2008.rpc.transport.command;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface Header {
    int getRequestId();
    void setRequestId(int requestId);
    void setVersion(int version);
    int getVersion();
    void setType(int type);
    int getType();
}
