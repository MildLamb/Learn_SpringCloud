# 这里就是开始
- 启动IDEA，创建一个Maven项目
- 父总工程依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.mildlamb</groupId>
    <artifactId>springclould</artifactId>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>springcloud-api</module>
        <module>springcloud-provider-location-8001</module>
    </modules>

    <!-- 打包方式 -->
    <packaging>pom</packaging>

    <properties>
<!--        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>-->
<!--        <maven.compiler.source>1.8</maven.compiler.source>-->
<!--        <maven.compiler.target>1.8</maven.compiler.target>-->
        <junit.version>4.12</junit.version>
        <lombok.version>1.18.22</lombok.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- springcloud依赖 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>2020.0.4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- SpringBoot依赖 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.5.6</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- Springboot启动器 -->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.2.0</version>
            </dependency>
            <!-- 数据库 -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.27</version>
            </dependency>
            <!-- 数据源 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.2.8</version>
            </dependency>
            <!-- junit -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>
            <!-- lombok -->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>
            <!-- log4j -->
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>1.2.17</version>
            </dependency>

            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-core</artifactId>
                <version>1.2.7</version>
            </dependency>
        </dependencies>
    </dependencyManagement>


</project>
```
___
- 创建一个实体类和数据库
- 创建一个api模块
- api模块依赖
```xml
<!-- 当前的Module自己需要的依赖，如果父工程中已经配置了版本，这里就不用配置版本了 -->
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```
___
- 创建一个提供者模块 provider-8001
- 所需依赖
```xml
<dependencies>
  <!-- 从springcloud-api获得实体类 -->
  <dependency>
      <groupId>org.mildlamb</groupId>
      <artifactId>springcloud-api</artifactId>
      <version>1.0-SNAPSHOT</version>
  </dependency>
  <!-- junit -->
  <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
  </dependency>
  <!-- 数据库 -->
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
  </dependency>
  <!-- 数据源 -->
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
  </dependency>
  <!-- 日志门面 -->
  <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-core</artifactId>
  </dependency>
  <!-- mybatis -->
  <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
  </dependency>
  <!-- springboot-test -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-test</artifactId>
  </dependency>
  <!-- web场景启动器 -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <!-- jetty服务器 -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jetty</artifactId>
  </dependency>
</dependencies>
```
- application.yml
```yml
server:
  port: 8001

# mybatis配置
mybatis:
  type-aliases-package: com.mildlamb.springcloud.pojo
  mapper-locations: classpath:mybatis/mapper/*.xml
  config-location: classpath:mybatis/mybatis-config.xml

# spring的配置
spring:
  application:
    name: springcloud-provider-location
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  #数据源
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cloud01?useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
```
