package com.mizore.easybuy.mapper;

import com.mizore.easybuy.model.entity.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

    List<TbUser> login(String username, String password);

    boolean register_user(TbUser user);

    List<TbUser> username_exist(String username);
}
