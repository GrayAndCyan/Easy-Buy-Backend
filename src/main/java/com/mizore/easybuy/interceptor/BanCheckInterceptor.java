package com.mizore.easybuy.interceptor;

import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@AllArgsConstructor
public class BanCheckInterceptor implements HandlerInterceptor {


    private
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO userDTO = UserHolder.get();
        if (userDTO == null) {
            return true;
        }

        // 查用户是否被ban，是则检验解封时间

    }
}
