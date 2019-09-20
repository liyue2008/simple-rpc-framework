package com.github.liyue2008.rpc.serialize;

/**
 * @author LiYue
 * Date: 2019-08-12
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
    public SerializeException(Throwable throwable){ super(throwable);}
}
