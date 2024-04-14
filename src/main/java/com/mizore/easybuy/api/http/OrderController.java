package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
