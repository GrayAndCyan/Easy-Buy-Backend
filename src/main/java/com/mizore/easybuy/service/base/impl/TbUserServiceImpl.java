package com.mizore.easybuy.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mizore.easybuy.mapper.TbAddressMapper;
import com.mizore.easybuy.mapper.TbSellerMapper;
import com.mizore.easybuy.mapper.TbUserMapper;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.*;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.enums.RoleEnum;
import com.mizore.easybuy.model.vo.*;
import com.mizore.easybuy.service.base.*;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
@Slf4j
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

    @Autowired
    private TbSellerMapper tbSellerMapper;

    @Override
    public TbUser getByUser(int userId) {
        LambdaQueryWrapper<TbUser> query = new LambdaQueryWrapper<TbUser>()
                .eq(TbUser::getId, userId);
        List<TbUser> res = list(query);
        if (res != null && res.size() > 1) {
            log.error("存在脏数据 buyer: {}", Arrays.toString(res.toArray()));
        }
        return CollectionUtil.isEmpty(res) ? null : res.get(0);
    }

    /**
     * 用户开店
     */
    @Transactional
    public BaseVO openStore(String name, String address) {
        BaseVO<String> baseVO = new BaseVO<>();
        // 新增店铺（卖家）
        TbSeller tbSeller = new TbSeller();
        // 获取当前登录用户
        UserDTO userDTO = UserHolder.get();
        // 判断当前用户角色是否为普通买家，若是，则能够开店，赋予其卖家角色
        if(RoleEnum.BUYER.getCode().equals(userDTO.getRole())){
            // 设置卖家对应的用户账号id
            tbSeller.setUserId(userDTO.getId());
            // 设置店铺名称
            tbSeller.setName(name);
            // 设置店铺地址
            tbSeller.setAddress(address);
            // 将新增店铺信息插入数据库
            tbSellerMapper.insert(tbSeller);
            // 赋予用户卖家角色，更新数据库中用户信息
            update().set("role", RoleEnum.SELLER.getCode())
                    .eq("id",userDTO.getId())
                    .eq("role", RoleEnum.BUYER.getCode())
                    .update();
            return baseVO.success();
        } else {
            baseVO.setCode(ReturnEnum.FAILURE.getCode());
            baseVO.setMessage("Open store failed, role error!");
            return baseVO;
        }
    }

    @Autowired
    private TbAddressMapper tbAddressMapper;
    @Transactional
    public BaseVO addAdd(String adddes,String addname,String addphone){
        BaseVO<String> baseVO = new BaseVO<>();
        TbAddress tbAddress = new TbAddress();
        UserDTO userDTO = UserHolder.get();

        tbAddress.setUserId(userDTO.getId());
        tbAddress.setAddrDesc(adddes);
        tbAddress.setAddrUsername(addname);
        tbAddress.setAddrPhone(addphone);
        tbAddressMapper.insert(tbAddress);
        return baseVO.success();
    }

    @Override
    public TbUser getUserByLoginInfo(String username, String password) {
        LambdaQueryWrapper<TbUser> query = new LambdaQueryWrapper<TbUser>()
                .eq(TbUser::getUsername, username)
                .eq(TbUser::getPassword, password);
        return list(query).get(0);
    }
}
