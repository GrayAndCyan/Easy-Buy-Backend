package com.mizore.easybuy.service.business;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.model.enums.RoleEnum;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.utils.JWTUtil;
import com.mizore.easybuy.utils.UserHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ITbUserService tbUserService;


    public BaseVO<String> login(String username, String password, HttpServletResponse response) {
        BaseVO<String> baseVO = new BaseVO<>();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            log.warn("登陆参数异常， username:{}, password:{} ", username, password);
            return baseVO.failure();
        }

        List<TbUser> res = tbUserService.list(new LambdaQueryWrapper<TbUser>()
                .eq(TbUser::getUsername, username)
                .eq(TbUser::getPassword, password)
        );

        if (CollectionUtil.isEmpty(res) || res.size() != 1) {
            log.info("username: {},password: {}, list size: {}", username, password, res.size());
            return baseVO.failure().setData("登陆失败，用户不存在。");
        }

        // 登陆成功，生成token并返回
        TbUser user = res.get(0);
        Map<String, Object> newClaims = Maps.newHashMap();
        newClaims.put("id", user.getId());
        newClaims.put("username", user.getUsername());
        newClaims.put("role", user.getRole());
        String token = JWTUtil.generateJWT(newClaims);
        response.setHeader("token", token);  // 在响应头返回token
        return baseVO.setData(token);  // 响应体的data再存一遍token ，前端可以从响应体/响应头 两个地方拿到token
    }

    public BaseVO<Object> register(String username, String password) {
        BaseVO<Object> baseVO = new BaseVO<>();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            log.warn("注册参数异常， username:{}, password:{} ", username, password);
            return baseVO.failure();
        }

        if (tbUserService.exists(new QueryWrapper<TbUser>().eq("username", username))) {
            return baseVO.failure().setData("已存在该username");
        }

        // sign up
        TbUser toSave = new TbUser();
        toSave.setRole(RoleEnum.BUYER.getCode());
        toSave.setUsername(username);
        toSave.setPassword(password);
        tbUserService.save(toSave);

        return baseVO.success();
    }
}
