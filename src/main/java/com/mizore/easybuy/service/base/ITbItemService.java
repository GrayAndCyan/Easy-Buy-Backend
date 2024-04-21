package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbItemService extends IService<TbItem> {

    // 卖家根据条件查询满足条件的商品
    List<TbItem> conditionalGetItems(Integer categoryId, List<Integer> statuses, String keyword, int sellerId);
}
