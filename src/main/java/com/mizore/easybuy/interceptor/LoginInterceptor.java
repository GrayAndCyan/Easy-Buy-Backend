package com.mizore.easybuy.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.utils.JWTUtil;
import com.mizore.easybuy.utils.UserHolder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final static String TOKEN = "token";

    private ITbUserService userService;

    public LoginInterceptor(ITbUserService userService) {
        this.userService = userService;
    }

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

        Integer id = claims.get("id", Integer.class);
        String username = claims.get("username", String.class);
        Integer role = claims.get("role", Integer.class);
        if (id == null || username == null || role == null) {
            log.error("未从token中解析得到需要的信息, token: {}, id: {}, username: {}, role: {}",
                    token, id, username, role);
            writeFailedMsg(response, "token错误。");
            return false;
        }

        // 根据id去db查user，因为jwt-payload中的信息可能为脏数据，此时就需要换token
        TbUser dbUser = userService.getById(id);
        if (dbUser == null) {
            log.error("未查询到 id = {} 用户", id);
            writeFailedMsg(response, "token中id错误。");
            return false;
        }

        String dbUsername = dbUser.getUsername();
        Integer dbRole = dbUser.getRole();
        UserDTO loginUser;
        // db与token一致
        if (username.equals(dbUsername) && role.equals(dbRole)) {
            loginUser = UserDTO.builder()
                    .id(id)
                    .username(username)
                    .role(role)
                    .build();
            UserHolder.save(loginUser);
            return true;
        }

        // 不一致，重新生成
        loginUser = UserDTO.builder()
                .id(id)
                .username(dbUsername)
                .role(dbRole)
                .build();
        Map<String, Object> newClaims = Maps.newHashMap();
        newClaims.put("id", id);
        newClaims.put("username", dbUsername);
        newClaims.put("role", dbRole);
        String newToken = JWTUtil.generateJWT(newClaims);
        response.setHeader(TOKEN, newToken);  // 前端需以此更新token
        UserHolder.save(loginUser);
        return true;
    }

    // 在响应体中写指定失败msg
    private void writeFailedMsg(HttpServletResponse response, String s) {
        BaseVO<String> baseVO = new BaseVO<String>().failure();
        baseVO.setData(s);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(JSONUtil.toJsonStr(baseVO).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 每次请求清理 tl, 防内存泄漏
        UserHolder.remove();
    }
}
