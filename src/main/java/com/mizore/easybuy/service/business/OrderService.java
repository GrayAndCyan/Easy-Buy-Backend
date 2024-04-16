package com.mizore.easybuy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.*;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.OrderDetailVO;
import com.mizore.easybuy.model.vo.OrderInfo4SellerVO;
import com.mizore.easybuy.model.vo.PageVO;
import com.mizore.easybuy.service.base.*;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbAddressService tbAddressService;

    @Autowired
    private ITbOrderDetailService tbOrderDetailService;

    @Autowired
    private ITbItemService tbItemService;

    @Autowired
    private ITbItemImageService tbItemImageService;

    @Autowired
    private ITbSellerService tbSellerService;

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

    public BasePageVO<List<OrderInfo4SellerVO>> search(Integer orderId, Integer userId, List<Integer> statuses, Integer pageSize, Integer pageNum) {
        if (pageSize == null || pageNum == null) {
            pageSize = 1;
            pageNum = 10;
        }

        Page<Object> resPage = PageHelper.startPage(1, 10);

        UserDTO userDTO = UserHolder.get();
        // 查对应店铺
        int sellerId = -1;
        if (userDTO != null) {
            int id = userDTO.getId();
            TbSeller seller = tbSellerService.getByOwner(id);
            if (seller != null) {
                sellerId = seller.getId();
            }
        }

        List<TbOrder> tbOrders = tbOrderService.search(orderId, userId, statuses, sellerId);

        // userId -> username
        List<Integer> userIds = tbOrders.stream()
                .map(TbOrder::getUserId)
                .toList();
        Map<Integer, String> userMap;
        if (CollectionUtil.isEmpty(userIds)) {
            userMap = Maps.newHashMap();
            log.error("empty user ids");
        } else {
            List<TbUser> tbUsers = tbUserService.listByIds(userIds);
            userMap = tbUsers.stream()
                    .collect(Collectors.toMap(TbUser::getId, TbUser::getUsername));
        }

        // addrId -> addr
        List<Integer> addrIds = tbOrders.stream()
                .map(TbOrder::getAddressId)
                .toList();
        Map<Integer, TbAddress> addressMap;
        if (CollectionUtil.isEmpty(addrIds)) {
            addressMap = Maps.newHashMap();
            log.error("empty addr ids");
        } else {
            List<TbAddress> tbAddresses = tbAddressService.listByIds(addrIds);
            addressMap = tbAddresses.stream()
                    .collect(Collectors.toMap(TbAddress::getId, x -> x));
        }

        List<Integer> orderIds = tbOrders.stream()
                .map(TbOrder::getId)
                .toList();
        Map<Integer, List<OrderDetailVO>> orderDetialVOMap = Maps.newHashMap();
        Map<Integer, List<TbOrderDetail>> orderDetialMap = Maps.newHashMap();
        Map<Integer, TbItem> itemMap = Maps.newHashMap();
        if (CollectionUtil.isEmpty(orderIds)) {
            log.error("empty order ids");
        } else {
            // orderId -> List<OrderDetailVO>
            List<TbOrderDetail> tbOrderDetails = tbOrderDetailService.listByOrders(orderIds);
            // 先批查出涉及的item信息
            Set<Integer> itemIds = tbOrderDetails.stream()
                    .map(TbOrderDetail::getItemId)
                    .collect(Collectors.toSet());
            if (CollectionUtil.isEmpty(itemIds)) {
                log.error("empty item ids");
                itemMap = Maps.newHashMap();
            } else {
                List<TbItem> tbItems = tbItemService.listByIds(itemIds);
                itemMap = tbItems.stream().collect(Collectors.toMap(TbItem::getId, x -> x));
                // 按照orderId分组
                orderDetialMap = tbOrderDetails.stream()
                        .collect(Collectors.groupingBy(TbOrderDetail::getOrderId));
            }
        }
        for (Map.Entry<Integer, List<TbOrderDetail>> orderEntry : orderDetialMap.entrySet()) {
            // 订单粒度
            List<TbOrderDetail> detailItems = orderEntry.getValue();
            Map<Integer, TbItem> finalItemMap = itemMap;
            List<OrderDetailVO> detailVOS = detailItems.stream().map(
                    // 订单单品明细粒度
                    x -> {
                        Integer itemId = x.getItemId();
                        TbItem tbItem = finalItemMap.get(itemId);
                        return OrderDetailVO.builder()
                                .itemId(itemId)
                                .imgUrl(tbItemImageService.getFirstImgUrl(itemId)) // todo 可优化
                                .itemDesc(tbItem.getDescription())
                                .unitPrice(tbItem.getPrice())
                                .subtotal(x.getSubtotal())
                                .quantity(x.getQuantity())
                                .build();
                    }
            ).toList();
            orderDetialVOMap.put(orderEntry.getKey(), detailVOS);
        }

        // convert to VO
        List<OrderInfo4SellerVO> res = Lists.newArrayList();
        for (TbOrder tbOrder : tbOrders) {
            Integer orderStatus = tbOrder.getStatus();
            TbAddress tbAddress = addressMap.get(tbOrder.getAddressId());
            Integer tbOrderId = tbOrder.getId();
            OrderInfo4SellerVO orderInfo = OrderInfo4SellerVO.builder()
                    .content(orderDetialVOMap.getOrDefault(tbOrderId, Lists.newArrayList()))
                    .id(tbOrderId)
                    .ctime(tbOrder.getCtime())

                    .status(orderStatus)
                    .statusDesc(OrderStatusEnum.getDescByCode(orderStatus))

                    .userId(tbOrder.getUserId())
                    .username(userMap.getOrDefault(tbOrder.getUserId(), StrUtil.EMPTY))

                    .addressId(tbOrder.getAddressId())
                    .addrDesc(tbAddress != null ? tbAddress.getAddrDesc() : StrUtil.EMPTY)
                    .addrUsername(tbAddress != null ? tbAddress.getAddrUsername() : StrUtil.EMPTY)
                    .addrPhone(tbAddress != null ? tbAddress.getAddrPhone() : StrUtil.EMPTY)
                    .build();
            res.add(orderInfo);
        }

        BasePageVO<List<OrderInfo4SellerVO>> basePageVO = new BasePageVO<List<OrderInfo4SellerVO>>().success();
        basePageVO.setData(res);
        PageVO pageVO = new PageVO(pageSize, pageNum);
        pageVO.setPages(resPage.getPages());
        pageVO.setTotal(resPage.getTotal());
        basePageVO.setPage(pageVO);
        return basePageVO;
    }

    public void sendOrder(Integer orderId) {
        TbOrder order = tbOrderService.getById(orderId);
        if (order != null && Objects.equals(order.getStatus(), OrderStatusEnum.PROCESSING.getCode())) {
            order.setStatus(OrderStatusEnum.SHIPPED.getCode());
            tbOrderService.updateById(order);
        }
    }
}
