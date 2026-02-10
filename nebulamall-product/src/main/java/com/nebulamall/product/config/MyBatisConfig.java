package com.nebulamall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis-Plus Configuration
 * 配置 MyBatis-Plus 的分页插件等功能
 *
 * @author zyz
 * @email
 * @date 2026-02-01 04:52:39
 */
@Configuration // 标记这是一个配置类
@EnableTransactionManagement // 启用事务管理
@MapperScan("com.nebulamall.product.dao") // 扫描 Mapper 接口所在的包
public class MyBatisConfig
{
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求的页面大于最大页后操作，true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(true);
        //设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(1000);

        return paginationInterceptor;
    }

}
