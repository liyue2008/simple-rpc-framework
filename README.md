# 消息队列高手课：动手实现一个简单的RPC框架

极客时间[《消息队列高手课》](https://time.geekbang.org/column/intro/212)案例篇《动手实现一个简单的RPC框架》示例源代码。

## 环境要求

运行示例之前需要先安装：

* JDK 1.8
* Maven 3.3.9

```bash
$java -version
java version "1.8.0_202"
Java(TM) SE Runtime Environment (build 1.8.0_202-b08)
Java HotSpot(TM) 64-Bit Server VM (build 25.202-b08, mixed mode)

$mvn -version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
```

## 下载编译源代码

```bash
$git clone git@github.com:liyue2008/simple-rpc-framework.git
$cd simple-rpc-framework
$mvn package
```

## 启动服务端

```bash
$java -jar server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar
[main] INFO com.github.liyue2008.rpc.server.Server - 创建并启动RpcAccessPoint...
[main] INFO com.github.liyue2008.rpc.transport.RequestHandlerRegistry - Load request handler, type: 0, class: com.github.liyue2008.rpc.transport.RpcRequestHandler.
[main] INFO com.github.liyue2008.rpc.server.Server - 向RpcAccessPoint注册com.github.liyue2008.rpc.hello.HelloService服务...
[main] INFO com.github.liyue2008.rpc.server.Server - 服务名: com.github.liyue2008.rpc.hello.HelloService, 向NameService注册...
[main] INFO com.github.liyue2008.rpc.nameservice.LocalFileNameService - Register service: com.github.liyue2008.rpc.hello.HelloService, uri: rpc://localhost:9999.
[main] INFO com.github.liyue2008.rpc.serialize.SerializeSupport - Found serializer, class: com.github.liyue2008.rpc.nameservice.Metadata, type: 100.
[main] INFO com.github.liyue2008.rpc.serialize.SerializeSupport - Found serializer, class: java.lang.String, type: 0.
[main] INFO com.github.liyue2008.rpc.serialize.SerializeSupport - Found serializer, class: com.github.liyue2008.rpc.client.stubs.RpcRequest, type: 101.
[main] INFO com.github.liyue2008.rpc.nameservice.LocalFileNameService - Metadata:
	Classname: com.github.liyue2008.rpc.hello.HelloService
	URIs:
		rpc://localhost:9999

[main] INFO com.github.liyue2008.rpc.server.Server - 开始提供服务，按任何键退出.
```

## 运行客户端

```bash
java -jar client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar
[main] INFO com.github.liyue2008.rpc.serialize.SerializeSupport - Found serializer, class: com.github.liyue2008.rpc.nameservice.Metadata, type: 100.
[main] INFO com.github.liyue2008.rpc.serialize.SerializeSupport - Found serializer, class: java.lang.String, type: 0.
[main] INFO com.github.liyue2008.rpc.serialize.SerializeSupport - Found serializer, class: com.github.liyue2008.rpc.client.stubs.RpcRequest, type: 101.
[main] INFO com.github.liyue2008.rpc.nameservice.LocalFileNameService - Metadata:
	Classname: com.github.liyue2008.rpc.hello.HelloService
	URIs:
		rpc://localhost:9999

[main] INFO com.github.liyue2008.rpc.client.Client - 找到服务com.github.liyue2008.rpc.hello.HelloService，提供者: rpc://localhost:9999.
[main] INFO com.github.liyue2008.rpc.client.Client - 请求服务, name: Master MQ...
[main] INFO com.github.liyue2008.rpc.client.Client - 收到响应: Hello, Master MQ.
```
