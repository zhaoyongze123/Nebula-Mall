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

        // 允许跨域的源 - 指定具体的来源（支持本地开发的多个端口）
        corsConfig.addAllowedOrigin("http://localhost:8001");
        corsConfig.addAllowedOrigin("http://localhost:8080");
        corsConfig.addAllowedOrigin("http://localhost:3000");

        // 允许的 HTTP 方法 - 明确列出所有方法
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("OPTIONS");

        // 允许的请求头
        corsConfig.addAllowedHeader("*");
        
        // 暴露的响应头
        corsConfig.addExposedHeader("Authorization");
        corsConfig.addExposedHeader("Content-Type");

        // 允许发送 Cookie - 当使用具体源时可以设置为 true
        corsConfig.setAllowCredentials(true);

        corsConfig.setMaxAge(3600L);

        // 为所有路径应用配置
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}