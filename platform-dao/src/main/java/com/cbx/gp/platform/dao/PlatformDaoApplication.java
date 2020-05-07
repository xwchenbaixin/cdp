package com.cbx.gp.platform.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.cbx.gp.platform.dao.mapper")
public class PlatformDaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformDaoApplication.class, args);
    }

}
