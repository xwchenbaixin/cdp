package com.cbx.gp.platform.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.cbx.gp.platform.dao")
@EnableDubboConfig
public class PlatformServiceImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformServiceImplApplication.class, args);
    }

}
