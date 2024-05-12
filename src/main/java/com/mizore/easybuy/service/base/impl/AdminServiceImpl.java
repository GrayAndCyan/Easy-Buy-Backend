package com.mizore.easybuy.service.base.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mizore.easybuy.mapper.AdminMapper;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.query.PageQuery;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ItemServiceImpl itemService;

    /**
     * 管理员查询用户列表
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult userList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<UserDTO> page = adminMapper.userList(pageQuery);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 管理员根据卖家id移除卖家身份
     * @param id
     */
    @Transactional
    @Override
    public void sellerRemove(Integer id) {
        int userId = adminMapper.getUserIdById(id);
        adminMapper.sellerRemove(id);
        adminMapper.userRemove(userId);
        itemService.deleteItemBySellerId(id);
    }
}
