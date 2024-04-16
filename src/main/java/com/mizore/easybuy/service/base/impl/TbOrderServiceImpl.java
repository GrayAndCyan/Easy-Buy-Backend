package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.mapper.TbOrderMapper;
import com.mizore.easybuy.service.base.ITbOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements ITbOrderService {

    @Override
    public List<TbOrder> search(Integer orderId, Integer userId, List<Integer> status, int sellerId) {
        LambdaQueryWrapper<TbOrder> query = new LambdaQueryWrapper<TbOrder>()
                .eq(TbOrder::getSellerId, sellerId);

        if (orderId != null) {
            query.eq(TbOrder::getId, orderId);
        }

        if (userId != null) {
            query.eq(TbOrder::getUserId, userId);
        }

        if (status != null) {
            query.in(TbOrder::getStatus, status);
        }

        return list(query);
    }
}
