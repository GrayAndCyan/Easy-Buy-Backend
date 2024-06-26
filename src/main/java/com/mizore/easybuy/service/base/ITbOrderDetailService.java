package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbOrderDetailService extends IService<TbOrderDetail> {

    List<TbOrderDetail> listByOrders(List<Integer> orderIds);
}
