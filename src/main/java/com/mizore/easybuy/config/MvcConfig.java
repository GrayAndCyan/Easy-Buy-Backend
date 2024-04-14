package com.mizore.easybuy.config;

import com.mizore.easybuy.interceptor.LoginInterceptor;
import com.mizore.easybuy.service.base.ITbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Autowired
    private ITbUserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(userService))
                .excludePathPatterns(
                        "/user/login",
                        "/user/sign"
                );  // todo 排除登陆注册接口
    }
}
