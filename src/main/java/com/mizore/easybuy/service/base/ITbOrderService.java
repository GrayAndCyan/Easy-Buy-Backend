package com.mizore.easybuy.service.base;

import com.mizore.easybuy.model.entity.TbOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
public interface ITbOrderService extends IService<TbOrder> {

    List<TbOrder> search(Integer orderId, Integer userId, List<Integer> status, int sellerId);

    List<TbOrder> buyerSearch(Integer orderId, Integer sellerId, List<Integer> status, int userId);

    List<TbOrder> searchforuser(Integer orderId, Integer userId, List<Integer> status);
}
