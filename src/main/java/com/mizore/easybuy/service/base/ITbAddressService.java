package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 地址表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbAddressService extends IService<TbAddress> {

    int addAddress(TbAddress tbAddress);

    int updateAddress(TbAddress tbAddress);

    int deleteAddress(Integer id);

    TbAddress queryAddress(Integer id);

    int findOrInsertAddr(Integer userid, String address, String username, String phone);
}
