package com.mizore.easybuy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class BeansConfig {

    // 处理跨域
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("*"); // 允许所有源
        config.addAllowedOriginPattern("*"); // 允许所有源
        config.addAllowedHeader("*"); // 放行所有请求头
        config.addAllowedMethod("*"); //允许所有请求方法
        config.setAllowCredentials(true); // 允许跨域发送cookie
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
