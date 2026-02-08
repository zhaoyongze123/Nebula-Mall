package com.example.nebulathirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure.class
})
public class NebulaThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NebulaThirdPartyApplication.class, args);
    }

}
