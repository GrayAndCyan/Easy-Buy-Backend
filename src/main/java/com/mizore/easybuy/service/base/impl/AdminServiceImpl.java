package com.mizore.easybuy.service.base.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mizore.easybuy.mapper.AdminMapper;
import com.mizore.easybuy.model.dto.ReturnUserDto;
import com.mizore.easybuy.model.entity.UserReturn;
import com.mizore.easybuy.model.query.PageQuery;
import com.mizore.easybuy.model.vo.PageResult;
import com.mizore.easybuy.service.base.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ItemServiceImpl itemService;

    /**
     * 管理员查询用户列表
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult userList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<ReturnUserDto> page = adminMapper.userList(pageQuery);
        List<ReturnUserDto> list = page.getResult();
        List<UserReturn> res = new ArrayList<>(list.size());
        //给status赋值
        for(ReturnUserDto returnUserDto : list){
            if(adminMapper.getUserStatus(returnUserDto)!=null){
                UserReturn userReturn = new UserReturn(
                        returnUserDto.getId(), returnUserDto.getUsername(),
                        returnUserDto.getAddr_phone(),returnUserDto.getRole(), 2);
                res.add(userReturn);
            }else{
                UserReturn userReturn = new UserReturn(
                        returnUserDto.getId(), returnUserDto.getUsername(),
                        returnUserDto.getAddr_phone(),returnUserDto.getRole(),1);
                res.add(userReturn);
            }
        }
        return new PageResult(page.getTotal(),res);
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
