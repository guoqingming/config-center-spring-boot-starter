springboot Zookeeper 配置中心

背景

随着目前微服务日益普及，spring clould生态圈日渐完善，分布式服务的配置一致性要求也越来越高，一些开源的配置中心也应运而生，比较著名的有springcloud的config-server,百度的disconf,携程的Apollo（阿波罗），阿里的Diamond等

项目介绍

本项目是基于分布式注册中心zookeeper搭建的一个简易的配置中心，采用Apache的走哦keeper开源客户端curator，封装了一个spring-boot-starter组件

使用步骤

1.依赖引入 将项目克隆到本地，mvn install安装本地，在pom.xml引用即可。
```
 <dependency> 
   <groupId>com.qm.config</groupId> 
   <artifactId>config-spring-boot-starter</artifactId> 
   <version>1.0.0-SNAPSHOT</version>
 </dependency> 
```
2.配置指定

在项目配置文件application.properties或者application.yml配置
```
 #指定zookeeper地址 zk.url=127.0.0.1
 #指定应用名称 ，用于读取指定配置 
 spring.application.name=risk #风控系统 
 #指定环境，比如：dev，test,sit,prod等 
 spring.profiles.active=prod #指定环境

```

