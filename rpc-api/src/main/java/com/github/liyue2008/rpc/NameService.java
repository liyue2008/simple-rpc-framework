package com.github.liyue2008.rpc;

import java.io.IOException;
import java.net.URI;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface NameService {
    void registerService(String serviceName, URI uri) throws IOException;
    URI lookupService(String serviceName) throws IOException;
}
