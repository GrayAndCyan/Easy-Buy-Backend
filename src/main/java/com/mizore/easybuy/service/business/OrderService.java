package com.mizore.easybuy.service.business;

import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.model.entity.TbOrderDetail;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.base.ITbItemService;
import com.mizore.easybuy.service.base.ITbOrderDetailService;
import com.mizore.easybuy.service.base.ITbOrderService;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private ITbOrderDetailService tbOrderDetailService;

    @Autowired
    private ITbItemService tbItemService;

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

    @Transactional
    public BaseVO<Object> placeOrder(List<TbOrderDetail> orderedItems, Integer addressId) {
        BaseVO<Object> baseVO = new BaseVO<>();

        TbOrder tbOrder = doPlaceOrder(orderedItems, addressId);
        // 下单失败
        if (tbOrder == null) {
            baseVO.setCode(ReturnEnum.FAILURE.getCode());
            baseVO.setMessage("order failed");
            return baseVO;
        }
        // 下单成功
        baseVO.setCode(200)
                .setMessage("order succeed")
                .setData(tbOrder);
        return baseVO;
    }

    private TbOrder doPlaceOrder(List<TbOrderDetail> orderedItems, Integer addressId) {
        // 获得当前下单用户
        UserDTO userDTO = UserHolder.get();
        // 创建一份订单
        TbOrder tbOrder = new TbOrder();
        // 设置下单用户
        tbOrder.setUserId(userDTO.getId());
        // 设置订单状态为“待付款”
        tbOrder.setStatus(OrderStatusEnum.PENDING_PAYMENT.getCode());
        // 设置地址
        tbOrder.setAddressId(addressId);
        // 将订单插入数据库
        tbOrderService.save(tbOrder);
        // 获得订单id
        Integer orderId = tbOrder.getId();
        // 向订单明细表中插入各个订单明细项
        BigDecimal sum = BigDecimal.valueOf(0);
        for(TbOrderDetail orderDetail: orderedItems){
            // 根据商品id得到商品
            Integer itemId = orderDetail.getItemId();
            TbItem tbItem = tbItemService.getById(itemId);
            // 判断商品是否存在以及商品库存是否充足
            if(tbItem == null || tbItem.getStock() < orderDetail.getQuantity()){
                throw new RuntimeException("item doesn't exist or out of stock");
            }
            // 更新商品库存信息
            tbItem.setStock(tbItem.getStock() - orderDetail.getQuantity());
            tbItem.setMtime(LocalDateTime.now());
            tbItemService.updateById(tbItem);
            // 设置订单明细对应的订单id
            orderDetail.setOrderId(orderId);
            // 设置购买该商品的小计总额
            orderDetail.setSubtotal(orderDetail.getUnitPrice()
                    .multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            // 将订单明细插入订单明细表
            tbOrderDetailService.save(orderDetail);
            // 累加订单总额
            sum = sum.add(orderDetail.getSubtotal());
        }
        // 更新订单总额
        tbOrder.setTotalAmount(sum);
        tbOrder.setMtime(LocalDateTime.now());
        tbOrderService.updateById(tbOrder);
        // 返回订单信息
        return tbOrderService.getById(orderId);
    }
}
