package com.mizore.easybuy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mizore.easybuy.model.entity.TbOrderDetail;
import com.mizore.easybuy.mapper.TbOrderDetailMapper;
import com.mizore.easybuy.service.base.ITbOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
public class TbOrderDetailServiceImpl extends ServiceImpl<TbOrderDetailMapper, TbOrderDetail> implements ITbOrderDetailService {

    @Override
    public List<TbOrderDetail> listByOrders(List<Integer> orderIds) {
        LambdaQueryWrapper<TbOrderDetail> query = new LambdaQueryWrapper<TbOrderDetail>()
                .in(TbOrderDetail::getOrderId, orderIds);
        return list(query);
    }
}
