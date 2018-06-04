# orpc

#### 一个简单的基于netty的rpc调用例子

#### 打包

```bash
git clone https://github.com/imayou/orpc.git
cd orpc
mvn clean source:jar install
```


#### 使用
```xml
<dependency>
	<groupId>com.orpc</groupId>
	<artifactId>orpc</artifactId>
	<version>0.0.1</version>
</dependency>
```
> 以项目下的orpc-example为例说hystrix

* 将HystrixRequestContext注入到每次请求中。
* UserCommand实现HystrixCommand返回一个对象User
* 写断熔器的参数
* 写熔断代码

> 具体说明在代码注释里面[com.orpc.example.client.command.UserCommand](https://github.com/imayou/orpc/blob/master/orpc-example/rpc-hello-client/src/main/java/com/orpc/example/client/command/UserCommand.java)


#### 其他
服务注册是随服务端的启动，启动了一个内嵌的zookeeper，所以可以不需要本地安装zookeeper
