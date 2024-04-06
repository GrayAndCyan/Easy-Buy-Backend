package com.mizore.easybuy.service.base.impl;

import com.mizore.easybuy.model.entity.TbUser;
import com.mizore.easybuy.mapper.TbUserMapper;
import com.mizore.easybuy.service.base.ITbUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

}
