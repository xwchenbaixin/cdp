package com.cbx.gp.platform.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableDubboConfig
@SpringBootApplication
@ComponentScan("com.cbx.gp.platform")
public class PlatformWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformWebApplication.class, args);
    }

}
