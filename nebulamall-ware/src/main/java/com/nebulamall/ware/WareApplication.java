package com.nebulamall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 库存模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.nebulamall.ware.dao")
public class WareApplication {

    public static void main(String[] args) {
        SpringApplication.run(WareApplication.class, args);
    }

}
