package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbBan;
import com.mizore.easybuy.model.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.OrderInfo4SellerVO;
import com.mizore.easybuy.model.vo.OrderInfo4UserVO;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbUserService extends IService<TbUser> {
    TbUser getByUser(int userId);

    BaseVO openStore(String name, String address);
    BaseVO addAdd(String adddes,String addname,String addphone);

    TbUser getUserByLoginInfo(String username, String password);
}
