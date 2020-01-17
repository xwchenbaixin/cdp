package com.cbx.gp.platform.service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableDubbo
@SpringBootApplication
@MapperScan("com.cbx.gp.platform.dao")
public class PlatformServiceImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformServiceImplApplication.class, args);
    }

}
