
dubbo 使用注解方式
下单服务（提供者） @DubboService     配送服务（消费者）@DubboReference


本项目是  dubbo3.0.0 nacos：2.0.0
需启动nacos客户端  客户端的版本为2.1.0

功能是 在 order服务中 服务提供者 实现OrderDubboService
@DubboService(version = "1.0.0", group = "order-create")

消费端  查阅 参考
@DubboReference(version = "1.0.0", group = "storage-order")
public OrderDubboService orderDubboService;

加版本号 区分不同的消费版本

添加group属性 如果有多个dubbo接口实现类时  可以通过group区分定向调用具体的 dubbo接口实现类

111111 提供超时机制 timeout（提供方和消费方都可设置）

dubbo缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止Spring初始化完成，以便上线时，能及早发现问题，默认check=true。
通过check="false" 关闭检查

重试次数 默认两次（提供方和消费方都可设置）
消费者访问提供者，如果访问失败，则切换重试访问其它服务器，但重试会带来更长延迟。访问时间变长，用户的体验较差。多次重新访问服务有可能访问成功。
可通过retries="2"来设置重试次数（不含第一次）。 3次请求

22222== 集群容错机制 ===
https://blog.csdn.net/lyliyongblue/article/details/104943359
Failover Cluster 失败自动切换 可以设置重试次数 可以指定方法级别的重试（默认机制）

Failfast Cluster 快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。
@Reference(cluster = "failfast")
private DemoService demoService3;

Failsafe Cluster 失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。

Failback Cluster 失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。
Forking Cluster 并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过 forks="2" 来设置最大并行数。
Broadcast Cluster  广播调用所有提供者，逐个调用，任意一台报错则报错 [2]。通常用于通知所有提供者更新缓存或日志等本地资源信息。

333333== 负载均衡功能 ===
@Reference(loadbalance = "random")
private DemoService demoService3;
轮询 随机 最少活跃数（慢的提供者接收更少的请求）一致性hash（相同参数的请求总是发到同一个提供者）

44444 == 消费者可以直连提供者 绕过注册中心
@Reference(url = "dubbo://192.168.50.30:12345")
private DemoService demoService;

https://blog.csdn.net/lyliyongblue/article/details/104943359
55555 支持多种协议 多协议暴露
66666 多注册中心配置

服务分组  通过group
=============多版本=============
当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。

可以按照以下的步骤进行版本迁移：

     1 在低压力时间段，先升级一半提供者为新版本
     2 再将所有消费者升级为新版本
     3 然后将剩下的一半提供者升级为新版本
因为提供者肯定要先升级 否则调不了新服务呀   你升级所有的提供者  造成消费者调不了  你升级所有的消费者 没用呀 都使用不了新服务


=================================== 基于注解不支持分组聚合！！！！===================================


==============  回声检查 ============
回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
所有服务自动实现 EchoService 接口，只需将任意服务引用强制转型为 EchoService，即可使用。


=================== 参数回调 ===================


================== 本地存根，类似Feign的异常Handler ====================
首先在公共包中写实现类 实现dubbo接口
并做判断校验  如果校验不通过 则直接返回不再调用远程接口
如果校验通过 则调用远程接口

同时在消费端 开启本地存根功能为true
@DubboReference(version = "1.0.0", group = "storage-order", stub = "true")
public OrderDubboService orderDubboService;


springcloud的feign调用 是使用注解  @FeignClient(value=服务名   , fallback = xxxx.class)
全局异常的降级处理类

对特定的接口使用降级处理  可配合 @HystrixCommand进行降级



=========================== dubbo框架 ===========================
dubbo是一个服务治理框架 它有服务监控  服务注册发现  服务通信框架通信协议RPC   服务容错  服务负载均衡

dubbo的架构分层：

接口服务层service
配置层
服务代理层
服务注册层
路由层
监控层
远程调用层
信息交换层exchange
网络传输层 抽象netty
数据序列化层



RPC 的场景中 主要包含了 服务发现  负载 容错 网络传输 序列化
其中RPC协议就是指明了服务如何进行序列化和网络传输  这就是RPC的核心功能

RPC的调用流程如下
首先几个概念   客户端  客户端存根    服务端存根 服务端
客户端存根 存储服务器的地址信息 将客户端的请求信息打包成网络消息 再通过网络传输发送给服务端
服务端存根 接收客户端的请求消息并解包 然后再调用本地服务进行处理

流程：
客户端调用远程服务接口

客户端存根接收到请求信息后 将方法 入参信息 序列化 成网络传输的消息体
客户端存根再找到远程服务地址 将消息通过网络传输给服务端

服务端存根接收到请求消息进行解码 反序列化操作
服务端存根再将请求信息进行本地处理

服务端执行具体的业务逻辑，并将处理结果返回给服务端存根

接着就是服务端存根将返回信息进行序列化成网络传输的消息体
客户端存根接收消息 并解码反序列化

服务消费端最终拿到结果


======= 其实RPC框架的目标就是将这些步骤给封装起来，也就是将调用 编码解码这些操作给封装起来  让用户感觉像调用本地服务一样调用远程服务。====


================================ 接下来是 dubbo的调用流程 =========
1、启动容器 ，运行服务提供者
2、服务提供者在运行服务时，将自己提供的服务注册在注册中心上
3、服务消费者在启动服务时，去订阅注册中心上的所需服务
4、注册中心返回给消费端 提供者的服务地址列表 消费端接收后 缓存在本地 如果内容有变更，注册中心则基于长连接将变更推送给消费端
5、消费端基于 通过服务端地址列表 基于软负载均衡  选择一台进行调用 如果调用失败，再选另一台
6、服务提供者和消费者 调用次数和调用时间 定时每分钟都会发送给监控中心


dubbo的负载均衡   dubbo的容错机制
dubbo支持哪些协议
dubbo http http2(也就是dubbo3新支持的triple协议) hessian协议 rmi协议 grpc协议 reddis和memcache

dubbo默认的通信框架是netty


======================== dubbo的其他功能 ================
@DubboService 有token属性配置 令牌验证 在服务提供方    为空表示不开启  如果为true 表示随机生成动态令牌 否则使用静态令牌
令牌的作用是防止消费者绕过注册中心直接访问 保证注册中心的授权功能有效 如果使用点对点调用 需关闭令牌功能

> register

register 向指定注册中心注册 多个注册中心ID用逗号分割  如果不想将该服务注册到任何registry 可以将值设为   N/A

> deprecate

服务是否过时

> dynamic

服务是否动态注册 如果设为false 注册后将显示disable状态  需人工开启

> delay

延迟注册服务时间(毫秒) 设为-1 表示延迟到spring容器初始化完成时暴露服务

> async

是否默认异步执行   不可靠异步  只是忽略返回值  不阻塞执行线程

> proxy

生成动态代理方式  可选  jdk/javassist

提交到github20231029

# 注意alibaba还是apache会有影响
# com.alibaba.dubbo.rpc.Filter
# dubboContextFilter=com.huolieniao.filter.DubboContextFilter

# META-INF.dubbo





















