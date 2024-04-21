package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.enums.Result_login;
import com.mizore.easybuy.model.enums.Result_register;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbUserService;
import com.mizore.easybuy.service.business.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private UserService userService;

    /**
     * 用户“我要开店”
     * @param name 店铺名称
     * @param address 店铺地址
     * @return
     */
    @PostMapping("openstore")
    public BaseVO openStore(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "address") String address
    ){
        return tbUserService.openStore(name, address);
    }

    @PostMapping("/login")
    public BaseVO<String> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response){
        return userService.login(username, password, response);
    }

    @PostMapping("/register")
    public BaseVO<Object> register(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userService.register(username, password);
    }
}
