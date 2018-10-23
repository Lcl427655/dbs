package com.cn.dbs.listener;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.transaction.CuratorTransactionBridge;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        /*目前Curator有2.x.x和3.x.x两个系列的版本，支持不同版本的Zookeeper。
        其中Curator 2.x.x兼容Zookeeper的3.4.x和3.5.x。而Curator 3.x.x只兼容Zookeeper 3.5.x，
        并且提供了一些诸如动态重新配置、watch删除等新特性。

        项目组件
        名称	    描述
        Recipes	    Zookeeper典型应用场景的实现，这些实现是基于Curator Framework。
        Framework	Zookeeper API的高层封装，大大简化Zookeeper客户端编程，添加了例如Zookeeper连接管理、重试机制等。
        Utilities	为Zookeeper提供的各种实用程序。
        Client  	Zookeeper client的封装，用于取代原生的Zookeeper客户端（ZooKeeper类），提供一些非常有用的客户端特性。
        Errors      Curator如何处理错误，连接问题，可恢复的例外等。

        Maven依赖
        Curator的jar包已经发布到Maven中心，由以下几个artifact的组成。根据需要选择引入具体的artifact。但大多数情况下只用引入curator-recipes即可。

        GroupID/Org	        ArtifactID/Name	            描述
        org.apache.curator	curator-recipes	            所有典型应用场景。需要依赖client和framework，需设置自动获取依赖。
        org.apache.curator	curator-framework	        同组件中framework介绍。
        org.apache.curator	curator-client	            同组件中client介绍。
        org.apache.curator	curator-test	            包含TestingServer、TestingCluster和一些测试工具。
        org.apache.curator	curator-examples	        各种使用Curator特性的案例。
        org.apache.curator	curator-x-discovery	        在framework上构建的服务发现实现。
        org.apache.curator	curator-x-discoveryserver	可以和Curator Discovery一起使用的RESTful服务器。
        org.apache.curator	curator-x-rpc	            Curator framework和recipes非java环境的桥接。

        根据上面的描述，开发人员大多数情况下使用的都是curator-recipes的依赖*/


        //zookeeper报Unable to read additional data from server sessionid 0x0,
        //likely server has closed socket, closing socket connection and attempting reconnect的解决
        //可能集群中某台Zookeeper没有启动或者ip和主机名称没有在hosts中映射
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
        ServiceInstance<Object> service = ServiceInstance.builder().name("dbs").address(ip).port(port).build();
        ServiceDiscovery<Object> build = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/soa").build();
        //System.out.println("client.getNamespace() = " + client.getNamespace());
        build.registerService(service);
        build.start();
        System.out.println("=================================服务注册完成=================================");

        //===================================================================================================
        //===================================================================================================
        /**
         *创建节点的基本操作
         */
        //===================================================================================================
        //===================================================================================================
        //判断节点是否存在，不存在返回null
        //注意：该方法返回一个Stat实例，用于检查ZNode是否存在的操作.
        //可以调用额外的方法(监控或者后台处理)并在最后调用forPath( )指定要操作的ZNode
        Stat test = client.checkExists().forPath("/test");
        if(test == null){
            //创建个简单节点，初始化内容为空
            // (注意：如果没有设置节点属性，节点创建模式默认为持久化节点，内容默认为空)
            String s = client.create().forPath("/test");
            System.out.println("s = " + s);
        }

        Stat testPath = client.checkExists().forPath("/test/path");
        if (testPath == null) {
            //创建一个节点，附带初始化内容
            client.create().forPath("/test/path","init".getBytes());
        }

        //持久化并且带序列号,返回的节点的名字
        String s = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/test/path", "path".getBytes());
        System.out.println("s = " + s);

        //可以重复创建临时节点
        client.create()
                //自动在名称空间base下创建父节点tem
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/tem/path","init222".getBytes());

        //===================================================================================================
        //===================================================================================================
        /**
         * 获取节点操作
         */
        //===================================================================================================
        //===================================================================================================
        //返回该节点的数据
        byte[] bs = client.getData().forPath("/tem/path");
        System.out.println("bs = " + new String(bs,0,bs.length));

        //读取一个节点的数据内容，同时获取到该节点的stat
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/tem/path");
        System.out.println("bytes = " + new String(bytes,0,bytes.length));
        //打印结果bytes = init
        System.out.println("stat = " + stat);
        //打印结果stat = 4294967522,4294967527,1540273564620,1540273564927,1,0,0,72057601234763779,4,0,4294967522

        //节点下边没有内容，返回的为空byte数组
        byte[] bst = client.getData().forPath("/tem");
        System.out.println("bst = " + new String(bst,0,bst.length));

        //===================================================================================================
        //===================================================================================================
        /**
         * 删除节点操作
         */
        //===================================================================================================
        //===================================================================================================
        //删除一个节点
        //注意，此方法只能删除叶子节点，否则会抛出异常
        client.create().forPath("/abc");
        client.delete().forPath("/abc");

        //删除一个节点，并且递归删除其所有的子节点
        client.create().creatingParentContainersIfNeeded().forPath("/efd/lmn");
        client.delete().deletingChildrenIfNeeded().forPath("/efd");

        //删除一个节点，强制指定版本进行删除
        client.create().creatingParentContainersIfNeeded().forPath("/xyz");
        client.delete().withVersion(0).forPath("/xyz");

        //删除一个节点，强制保证删除
        //guaranteed()接口是一个保障措施，只要客户端会话有效，
        // 那么Curator会在后台持续进行删除操作，直到删除节点成功。
        client.create().creatingParentContainersIfNeeded().forPath("/uvw");
        client.delete().guaranteed().forPath("/uvw");

        //上面的多个流式接口是可以自由组合的，例如：
        //client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(10086).forPath("path");

        //===================================================================================================
        //===================================================================================================
        /**
         * 更新节点操作
         */
        //===================================================================================================
        //===================================================================================================
        //更新一个节点的数据内容
        //注意：该接口会返回一个Stat实例
        Stat stat1 = client.setData().forPath("/tem/path", "data11111".getBytes());
        System.out.println("stat1 = " + stat1);

        //更新一个节点的数据内容，强制指定版本进行更新
        Stat ss = new Stat();
        client.getData().storingStatIn(ss).forPath("/tem/path");
        Stat stat2 = client.setData().withVersion(ss.getVersion()).forPath("/tem/path", "data22222".getBytes());
        System.out.println("stat2 = " + stat2);

        //注意：该方法的返回值为List<String>,获得ZNode的子节点Path列表。
        // 可以调用额外的方法(监控、后台处理或者获取状态watch, background or get stat)
        // 并在最后调用forPath()指定要操作的父ZNode
        client.getChildren().forPath("/").forEach(System.out::println);

        //===================================================================================================
        //===================================================================================================
        /**
         * 事务
         */
        //===================================================================================================
        //===================================================================================================
        //CuratorFramework的实例包含inTransaction( )接口方法，
        // 调用此方法开启一个ZooKeeper事务. 可以复合create, setData, check, and/or delete
        // 等操作然后调用commit()作为一个原子操作提交。一个例子如下：

        client.inTransaction().check().forPath("/")//先检查某个路径是否存在
                .and()
                .create().withMode(CreateMode.EPHEMERAL).forPath("/transaction","data1".getBytes())
                .and()
                .setData().forPath("/transaction","data2".getBytes())
                .and()
                .commit();//没有commit则查询不到节点

        //===================================================================================================
        //===================================================================================================
        /**
         * 异步接口
         */
        //===================================================================================================
        //===================================================================================================
        //上面提到的创建、删除、更新、读取等方法都是同步的，Curator提供异步接口，
        // 引入了BackgroundCallback接口用于处理异步接口调用之后服务端返回的结果信息。
        // BackgroundCallback接口中一个重要的回调值为CuratorEvent，
        // 里面包含事件类型、响应吗和节点的详细信息。
        /*CuratorEventType
        事件类型	对应CuratorFramework实例的方法
        CREATE	    #create()
        DELETE	    #delete()
        EXISTS	    #checkExists()
        GET_DATA	#getData()
        SET_DATA	#setData()
        CHILDREN	#getChildren()
        SYNC	    #sync(String,Object)
        GET_ACL 	#getACL()
        SET_ACL	    #setACL()
        WATCHED	    #Watcher(Watcher)
        CLOSING	    #close()

        响应码(#getResultCode())
        响应码	 意义
        0	     OK，即调用成功
        -4	     ConnectionLoss，即客户端与服务端断开连接
        -110	 NodeExists，即节点已经存在
        -112	 SessionExpired，即会话过期*/
        Executor executor = Executors.newFixedThreadPool(2);
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground((curatorFramework, curatorEvent) -> {
                    System.out.println(
                            String.format("eventType:%s,resultCode:%s",
                                    curatorEvent.getType(),
                                    curatorEvent.getResultCode()));
                },executor)
                .forPath("/thread");

        //===================================================================================================
        //===================================================================================================
        /**
         * 高级特性
         */
        //===================================================================================================
        //===================================================================================================

        //===================================================================================================
        //===================================================================================================
        /**
         * 缓存
         */
        //===================================================================================================
        //===================================================================================================
        /*提醒：首先你必须添加curator-recipes依赖，下文仅仅对recipes一些特性的使用进行解释和举例，
        不打算进行源码级别的探讨

        重要提醒：强烈推荐使用ConnectionStateListener监控连接的状态，
        当连接状态为LOST，curator-recipes下的所有Api将会失效或者过期，
        尽管后面所有的例子都没有使用到ConnectionStateListener。

        缓存
        Zookeeper原生支持通过注册Watcher来进行事件监听，但是开发者需要反复注册(Watcher只能单次注册单次使用)。
        Cache是Curator中对事件监听的包装，可以看作是对事件监听的本地缓存视图，
        能够自动为开发者处理反复注册监听。Curator提供了三种Watcher(Cache)来监听结点的变化。

        Path Cache
        Path Cache用来监控一个ZNode的子节点. 当一个子节点增加， 更新，删除时，
        Path Cache会改变它的状态， 会包含最新的子节点， 子节点的数据和状态，
        而状态的更变将通过PathChildrenCacheListener通知。

        实际使用时会涉及到四个类：

        PathChildrenCache
        PathChildrenCacheEvent
        PathChildrenCacheListener
        ChildData
        通过下面的构造函数创建Path Cache:

        public PathChildrenCache(CuratorFramework client, String path, boolean cacheData)
        想使用cache，必须调用它的start方法，使用完后调用close方法。 可以设置StartMode来实现启动的模式，

        StartMode有下面几种：

        NORMAL：正常初始化。
        BUILD_INITIAL_CACHE：在调用start()之前会调用rebuild()。
        POST_INITIALIZED_EVENT： 当Cache初始化数据后发送一个PathChildrenCacheEvent.Type#INITIALIZED事件
        public void addListener(PathChildrenCacheListener listener)可以增加listener监听缓存的变化。

        getCurrentData()方法返回一个List<org.apache.curator.framework.recipes.cache.ChildData>对象，可以遍历所有的子节点。

        设置/更新、移除其实是使用client (CuratorFramework)来操作, 不通过PathChildrenCache操作：*/

        //构造PathChildrenCache
        PathChildrenCache cache = new PathChildrenCache(client, "/cache/temp", true);
        //开启缓存
        cache.start();
        //实现监听接口
        PathChildrenCacheListener cacheListener = (c, event) -> {
            System.out.println("事件类型：" + event.getType());
            if (null != event.getData()) {
                System.out.println("节点数据：" + event.getData().getPath() + " = " +
                        new String(event.getData().getData()));
            }
        };
        //注册监听事件
        cache.getListenable().addListener(cacheListener);
        //操作节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/cache/temp/test01", "01".getBytes());
        Thread.sleep(20000);
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/cache/temp/test02", "02".getBytes());
        Thread.sleep(20000);
        client.setData().forPath("/cache/temp/test01", "01_V2".getBytes());
        Thread.sleep(20000);
        for (ChildData data : cache.getCurrentData()) {
            System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData()));
        }
        client.delete().forPath("/cache/temp/test01");
        Thread.sleep(20000);
        client.delete().forPath("/cache/temp/test02");
        Thread.sleep(2000 * 5);
        cache.close();
        client.close();
        System.out.println("OK!");

    }
}
