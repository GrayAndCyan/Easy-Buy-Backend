package com.mizore.easybuy.interceptor;

import cn.hutool.core.util.StrUtil;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.utils.JWTUtil;
import com.mizore.easybuy.utils.UserHolder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final static String TOKEN = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(TOKEN);
        if (StrUtil.isBlank(token)) {
            // 无token,认证失败
            log.info("token为空，认证失败。request: {}", request);
            return true;  // todo 本该返回 false，此处为便于测试 不做拦截 返回 true
        }

        // 解析token,保存用户对象上下文
        Claims claims = JWTUtil.parseJWT(token);
        UserDTO loginUser = UserDTO.builder()
                .id((Integer) claims.getOrDefault("id", 0))
                .username((String) claims.getOrDefault("username", StrUtil.EMPTY))
                .role((Byte) claims.getOrDefault("role", 1))
                .build();
        UserHolder.save(loginUser);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 每次请求清理 tl, 防内存泄漏
        UserHolder.remove();
    }
}
