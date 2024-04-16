package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.entity.TbOrderDetail;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/buyer/confirm/{id}")
    public BaseVO<Object> confirmReceive(@PathVariable(value = "id")Integer orderId) {
        orderService.confirmReceive(orderId);
        return new BaseVO<>().success();
    }


    /**
     * 用户下单
     * @param orderedItems 订单中的物品列表
     * @param addressId 收货地址id
     * @return
     * @throws RuntimeException
     */
    @PostMapping
    public BaseVO<Object> placeOrder(
            @RequestBody List<TbOrderDetail> orderedItems,
            @RequestParam(value = "addressId") Integer addressId
            ){
        return orderService.placeOrder(orderedItems, addressId);
    }

}
