package com.github.liyue2008.rpc.transport.command;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface ResponseHeader extends Header {
    int getStatus();
    void setStatus(int status);
    String getError();
    void setError(String msg);


}
