package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mizore.easybuy.model.vo.BaseVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbUserService extends IService<TbUser> {
    BaseVO openStore(String name, String address);
}
