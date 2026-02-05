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
        
        // 允许的源
        corsConfig.addAllowedOrigin("*");
        
        // 允许的 HTTP 方法
        corsConfig.addAllowedMethod("*");
        
        // 允许的请求头
        corsConfig.addAllowedHeader("*");
        
        // 是否允许发送 Cookie
        corsConfig.setAllowCredentials(false);
        
        // 预检请求的最大缓存时间（秒）
        corsConfig.setMaxAge(3600L);
        
        // 允许暴露的响应头
        corsConfig.addExposedHeader("*");
        
        // 为所有路径应用 CORS 配置
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}
