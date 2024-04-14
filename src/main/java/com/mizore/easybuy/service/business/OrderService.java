package com.mizore.easybuy.service.business;

import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.service.base.ITbOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private ITbOrderService tbOrderService;

    public void confirmReceive(Integer orderId) {
        if (orderId == null) {
            log.error("orderId == null.");
            return;
        }

        TbOrder order = tbOrderService.getById(orderId);
        // 确认收获需要是已发货状态的订单
        Integer status = order.getStatus();
        if (!OrderStatusEnum.SHIPPED.getCode().equals(status)) {
            log.error("订单状态不符合预期： order: {}", order);
            return;
        }

        order.setStatus(OrderStatusEnum.DELIVERED.getCode());
        tbOrderService.updateById(order);
    }
}
