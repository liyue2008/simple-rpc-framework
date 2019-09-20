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
//        final Gson gson = new Gson();
//        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
//        File nameServiceFile = new File(tmpDirFile, "simple_rpc_name_service");
//        Map<String/*Class name*/, List<URI>/*Service URI list*/> metadata = readMetadata(nameServiceFile, gson);
//        String serviceClassName = HelloService.class.getCanonicalName();
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

//    private static Map<String, List<URI>> readMetadata(File nameServiceFile, Gson gson) throws FileNotFoundException {
//        Type type = new TypeToken< Map<String, List<URI>>>(){}.getType();
//        return gson.fromJson(new FileReader(nameServiceFile), type);
//    }
}
