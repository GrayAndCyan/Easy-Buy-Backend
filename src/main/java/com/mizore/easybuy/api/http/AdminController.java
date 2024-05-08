package com.mizore.easybuy.api.http;


import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.query.PageQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/user_list")
    public BaseVO<PageResult> UserList(PageQuery pageQuery){
        log.info("管理员查询用户列表：页码：{}，每页记录数：{}",pageQuery.getPageNum(),pageQuery.getPageSize());
        PageResult pageResult = adminService.userList(pageQuery);
        return new BaseVO<PageResult>().success(pageResult);
    }

    @PostMapping("/seller_remove/{id}")
    public BaseVO RemoveSeller(@PathVariable (value = "id") Integer id){
        log.info("管理员根据卖家id移除卖家角色身份，id为：{}",id);
        adminService.sellerRemove(id);
        return new BaseVO().success();
    }

}
