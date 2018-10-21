package com.cn.dbs.listener;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

//springboot应用启动之后将服务注册到Zookeeper
@Component
public class ServiceRegister implements ApplicationRunner {//CommandLineRunner

    @Autowired
    private Environment environment;

    @Value("${app.localhost}")
    private String ip;

    @Value("${app.port}")
    private Integer port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("============== " + environment.getProperty("zookeeper.address"));
        System.out.println("ip =============== " + ip);
        System.out.println("port =============== " + port);
        //连接不上，curator-x-discovery版本2.x.x适用于zookeeper3.4.x,
        //curator-x-discovery版本3.x.x以上 适用于zookeeper3.5.x

        //zookeeper报Unable to read additional data from server sessionid 0x0,
        //likely server has closed socket, closing socket connection and attempting reconnect的解决
        //可能集群中某台Zookeeper没有启动
        /*CuratorFramework client = CuratorFrameworkFactory.newClient(
                environment.getProperty("zookeeper.address"),
                60000,
                3000,
                new RetryOneTime(3000));
        client.start();
        client.blockUntilConnected();*/
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(environment.getProperty("zookeeper.address"))
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(new RetryOneTime(3000))
                        .namespace("base")
                        .build();
        client.start();
        client.blockUntilConnected();
        client.create()
                //自动在名称空间base下创建父节点tem
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/tem/path","init".getBytes());
        ServiceInstance<Object> service = ServiceInstance.builder().name("dbs").address(ip).port(port).build();
        ServiceDiscovery<Object> build = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/soa").build();
        System.out.println("client.getNamespace() = " + client.getNamespace());
        build.registerService(service);
        build.start();
        System.out.println("===========服务注册完成===========");
    }
}
