package com.cn.my.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//读取自定义配置文件classpath:MyConfig/MyConfig.properties
//自定义的文件路径
@PropertySource("classpath:MyConfig/MyConfig.properties")
//前缀
@ConfigurationProperties(prefix = "myconfig")
//纳入Spring容器
@Component
//需要set方法来注入配置值
public class MyConfig {

    private Integer port;

    private String username;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
