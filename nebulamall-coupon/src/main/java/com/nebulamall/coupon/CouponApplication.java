package com.nebulamall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 优惠券模块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.nebulamall.coupon.dao")
public class CouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }

}
