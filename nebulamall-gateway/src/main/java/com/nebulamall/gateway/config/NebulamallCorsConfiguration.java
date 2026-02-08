package com.nebulamall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * CORS Configuration for Nebula-Mall Gateway
 * Handles cross-origin requests for all microservices
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-03
 */
@Configuration
public class NebulamallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 1、允许跨域的源
        // 错误写法：corsConfig.addAllowedOrigin("*");
        // 正确写法：使用 Pattern 可以兼容 setAllowCredentials(true)
        corsConfig.addAllowedOriginPattern("*");

        // 2、允许的 HTTP 方法
        corsConfig.addAllowedMethod("*");

        // 3、允许的请求头
        corsConfig.addAllowedHeader("*");

        // 4、是否允许发送 Cookie（必须配合 OriginPattern 使用）
        corsConfig.setAllowCredentials(true);

        corsConfig.setMaxAge(3600L);

        // 为所有路径应用配置
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}