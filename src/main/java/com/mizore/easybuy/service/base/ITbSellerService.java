package com.mizore.easybuy.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mizore.easybuy.model.entity.TbSeller;

/**
 * <p>
 * 店铺表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbSellerService extends IService<TbSeller> {

    TbSeller getByOwner(int ownerId);
}
