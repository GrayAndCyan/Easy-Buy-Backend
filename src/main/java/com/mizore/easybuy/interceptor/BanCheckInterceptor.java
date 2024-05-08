package com.mizore.easybuy.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Charsets;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.service.business.BanService;
import com.mizore.easybuy.utils.UserHolder;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@AllArgsConstructor
public class BanCheckInterceptor implements HandlerInterceptor {


    private BanService banService;

    private ITbUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int userId;
        if ("/user/login".equals(request.getRequestURI())) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (StrUtil.isNotBlank(username)) {
                TbUser loginUser = userService.getUserByLoginInfo(username, password);
                userId = loginUser.getId();
            } else {
                log.warn("登陆参数有误： username: {} , password: {}", username, password);
                return false;
            }
        } else {
            UserDTO userDTO = UserHolder.get();
            if (userDTO == null) {
                return true;
            }
            userId = userDTO.getId();
        }

        // 查用户是否被ban
        boolean banning = banService.isBanning(userId);
        if (banning) {
            ServletOutputStream os = response.getOutputStream();
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            BaseVO<Object> baseVO = new BaseVO<>().failure().setMessage("违规封禁中...");
            String jsonRsp = JSONUtil.toJsonStr(baseVO);
            os.write(jsonRsp.getBytes(Charsets.UTF_8));
            return false;
        }
        // 正常 未处于封禁
        return true;
    }
}
