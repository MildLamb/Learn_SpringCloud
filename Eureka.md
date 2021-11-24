# Eureka服务注册与发现
- Netfilx在设计Eureka时，遵循的就是AP原则
- Eureka是Netfilx的一个子模块，也是核心模块之一。Eureka是一个基于REST的服务，用于定位服务，以实现云端中间层服务发现和故障转移
- Eureka包含两个组件：Eureka Server和Eureka Client
- Eureka Server提供服务注册，各个节点启动后，会在EurekaServer中进行注册，这样Eureka Server中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到
