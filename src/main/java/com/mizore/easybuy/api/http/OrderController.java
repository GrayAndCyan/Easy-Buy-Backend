package com.mizore.easybuy.api.http;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.mizore.easybuy.model.entity.TbOrderDetail;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.OrderInfo4BuyerVO;
import com.mizore.easybuy.model.vo.OrderInfo4SellerVO;
import com.mizore.easybuy.service.business.OrderService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
     * 按条件查订单，没有传的条件则表示不做条件过滤
     * @param orderId 订单号
     * @param userId 按用户
     * @param statuses 按订单状态 可以传多个 查多个状态的
     * @param pageSize 页大小
     * @param pageNum 页码
     */
    @GetMapping("/seller/search")
    public BasePageVO<List<OrderInfo4SellerVO>> searchOrder(
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "statuses", required = false) List<Integer> statuses,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        return orderService.search(orderId, userId, statuses, pageSize, pageNum);
    }

    // 卖家发货
    @PostMapping("/seller/send/{orderId}")
    public BaseVO<Object> sendOrder(@PathVariable(value = "orderId") Integer orderId) {
        orderService.sendOrder(orderId);
        return new BaseVO<>().success();
    }


    /**
     * 用户下单
     * @param orderedItems 订单中的物品列表
     * @param addrDesc 收货地址描述
     * @param addrUsername 地址对应的用户名（收货人）
     * @param addrPhone 手机号
     * @param sellerId 卖家id
     * @return
     * @throws RuntimeException
     */
    @PostMapping
    public BaseVO<Object> placeOrder(
            @RequestBody List<TbOrderDetail> orderedItems,
            @RequestParam(value = "addrDesc") String addrDesc,
            @RequestParam(value = "addrUsername") String addrUsername,
            @RequestParam(value = "addrPhone") String addrPhone,
            @RequestParam(value = "sellerId") Integer sellerId
            ) {
        return orderService.placeOrder(orderedItems, addrDesc, addrUsername, addrPhone, sellerId);
    }

    /**
     * 买家按条件查订单，没有传的条件则表示不做条件过滤
     * @param orderId 订单号
     * @param sellerId 按卖家
     * @param statuses 按订单状态 可以传多个 查多个状态的
     * @param pageSize 页大小
     * @param pageNum 页码
     */
    @GetMapping("/buyer/search")
    public BasePageVO<List<OrderInfo4BuyerVO>> buyerSearchOrder(
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "sellerId", required = false) Integer sellerId,
            @RequestParam(value = "statuses", required = false) List<Integer> statuses,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        return orderService.buyerSearch(orderId, sellerId, statuses, pageSize, pageNum);
    }

    // 查指定状态中的最新一条订单
    @GetMapping("/buyer/lastorder")
    public BaseVO<OrderInfo4BuyerVO> buyerSearchTheLastOrder(
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "statuses", required = false) List<Integer> statuses
    ) {
/*        if (CollectionUtil.isEmpty(statuses)) {
            // 没传就默认查一条待收货的
            statuses = Lists.newArrayList(OrderStatusEnum.SHIPPED.getCode());
        }*/
        BasePageVO<List<OrderInfo4BuyerVO>> basePageVO = orderService.buyerSearch(orderId, null, statuses, 1, Integer.MAX_VALUE);
        List<OrderInfo4BuyerVO> orders = basePageVO.getData();

        BaseVO<OrderInfo4BuyerVO> baseVO = new BaseVO<OrderInfo4BuyerVO>().success();
        if (CollectionUtil.isEmpty(orders)) {
            log.info("buyerSearchTheLastOrder#buyerSearchTheLastOrder: CollectionUtil.isEmpty(orders)");
            return baseVO;
        }
        baseVO.setData(orders.get(0));  // orders 按修改时间倒序，故取第一条即可
        return baseVO;
    }

}
