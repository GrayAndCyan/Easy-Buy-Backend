package com.mizore.easybuy.service.base.impl;

import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.mapper.TbOrderMapper;
import com.mizore.easybuy.service.base.ITbOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
