package com.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.*")
@EnableTransactionManagement
@MapperScan(value = {"com.cn.dbs.mapper"})
public class App {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        Binder binder = Binder.get(context.getEnvironment());
        // 绑定简单配置
        //MyConfig myConfig = binder.bind("myconfig", Bindable.of(MyConfig.class)).get();
        //System.out.println(myConfig.getUsername());
        //================
    }
}
