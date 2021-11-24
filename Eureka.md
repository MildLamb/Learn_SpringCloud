# Eureka服务注册与发现
- 什么是Eureka?
  - Netfilx在设计Eureka时，遵循的就是AP原则
  - Eureka是Netfilx的一个子模块，也是核心模块之一。Eureka是一个基于REST的服务，用于定位服务，以实现云端中间层服务发现和故障转移
  - Eureka包含两个组件：Eureka Server和Eureka Client
  - Eureka Server提供服务注册，各个节点启动后，会在EurekaServer中进行注册，这样Eureka Server中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到
  - Eureka Client是一个Java客户端，用于简化EurekaServer的交互，客户端同时也具备一个内置的，使用轮询负载算法的负载均衡器。在启动应用后，将会向EurekaServer发送心跳(默认周期为30s)。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除掉(默认周期为90s)
