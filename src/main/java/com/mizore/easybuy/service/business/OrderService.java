package com.mizore.easybuy.service.business;

import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.model.entity.TbOrderDetail;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.entity.*;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.vo.*;
import com.mizore.easybuy.service.base.ITbItemService;
import com.mizore.easybuy.service.base.ITbOrderDetailService;
import com.mizore.easybuy.service.base.ITbOrderService;
import com.mizore.easybuy.utils.UserHolder;
import com.mizore.easybuy.service.base.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private ITbOrderDetailService tbOrderDetailService;

    @Autowired
    private ITbItemService tbItemService;

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbAddressService tbAddressService;

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

    @Transactional
    public BaseVO<Object> placeOrder(List<TbOrderDetail> orderedItems,
                                     Integer addressId, Integer sellerId) {
        BaseVO<Object> baseVO = new BaseVO<>();
        /*
        // 处理地址相关，得到地址id
        UserDTO user = UserHolder.get();
        Integer addressId = tbAddressService.findOrInsertAddr(
                user.getId(), addrDesc, addrUserName, addrPhone);
         */

        TbOrder tbOrder = doPlaceOrder(orderedItems, addressId, sellerId);
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

    public TbOrder doPlaceOrder(List<TbOrderDetail> orderedItems, Integer addressId, Integer sellerId) {
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
        // 设置卖家id
        tbOrder.setSellerId(sellerId);
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


    public BasePageVO<List<OrderInfo4UserVO>> searchforuser(Integer orderId, Integer userId, List<Integer> statuses, Integer pageSize, Integer pageNum) {
        if (pageSize == null || pageNum == null) {
            pageSize = 1;
            pageNum = 10;
        }

        Page<Object> resPage = PageHelper.startPage(1, 10);

        UserDTO userDTO = UserHolder.get();

        if (userDTO != null) {
            int id = userDTO.getId();
            TbUser user = tbUserService.getById(id);
            if (user != null) {
                userId = user.getId();
            }
        }

        List<TbOrder> tbOrders = tbOrderService.searchforuser(orderId, userId, statuses);

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
        List<OrderInfo4UserVO> res = Lists.newArrayList();
        for (TbOrder tbOrder : tbOrders) {
            Integer orderStatus = tbOrder.getStatus();
            TbAddress tbAddress = addressMap.get(tbOrder.getAddressId());
            Integer tbOrderId = tbOrder.getId();
            OrderInfo4UserVO orderInfo = OrderInfo4UserVO.builder()
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

        BasePageVO<List<OrderInfo4UserVO>> basePageVO = new BasePageVO<List<OrderInfo4UserVO>>().success();
        basePageVO.setData(res);
        PageVO pageVO = new PageVO(pageSize, pageNum);
        pageVO.setPages(resPage.getPages());
        pageVO.setTotal(resPage.getTotal());
        basePageVO.setPage(pageVO);
        return basePageVO;
    }

    /*public void sendOrder(Integer orderId, String delinum) {
        TbOrder order = tbOrderService.getById(orderId);
        if (order != null && Objects.equals(order.getStatus(), OrderStatusEnum.PROCESSING.getCode())) {
            order.setStatus(OrderStatusEnum.SHIPPED.getCode());
            tbOrderService.updateById(order);
        }
    }*/
    public void sendOrder(Integer orderId, String delinum, String delicom) {
        TbOrder order = tbOrderService.getById(orderId);
        if (order != null && Objects.equals(order.getStatus(), OrderStatusEnum.PROCESSING.getCode())) {
            order.setStatus(OrderStatusEnum.SHIPPED.getCode());
            order.setDelinum(delinum); // 将 delinum 设置到订单实体类中的 deliveryNumber 字段
            order.setDelicom(delicom);
            tbOrderService.updateById(order);
        }
    }

    // 买家按条件查订单
    public BasePageVO<List<OrderInfo4BuyerVO>> buyerSearch(Integer orderId, Integer sellerId, List<Integer> statuses, Integer pageSize, Integer pageNum) {
        if (pageSize == null || pageNum == null) {
            pageSize = 1;
            pageNum = 10;
        }

        Page<Object> resPage = PageHelper.startPage(1, 10);

        UserDTO userDTO = UserHolder.get();
        // 获取当前用户id
        int userId = userDTO.getId();

        List<TbOrder> tbOrders = tbOrderService.buyerSearch(orderId, sellerId, statuses, userId);

        // sellerID -> sellerName
        List<Integer> sellerIds = tbOrders.stream()
                .map(TbOrder::getSellerId)
                .toList();
        Map<Integer, String> sellerMap;
        if (CollectionUtil.isEmpty(sellerIds)) {
            sellerMap = Maps.newHashMap();
            log.error("empty seller ids");
        } else {
            List<TbSeller> tbSellers = tbSellerService.searchBySellerIds(sellerIds);
            sellerMap = tbSellers.stream()
                    .collect(Collectors.toMap(TbSeller::getId, TbSeller::getName));
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
        List<OrderInfo4BuyerVO> res = Lists.newArrayList();
        for (TbOrder tbOrder : tbOrders) {
            Integer orderStatus = tbOrder.getStatus();
            TbAddress tbAddress = addressMap.get(tbOrder.getAddressId());
            Integer tbOrderId = tbOrder.getId();
            OrderInfo4BuyerVO orderInfo = OrderInfo4BuyerVO.builder()
                    .content(orderDetialVOMap.getOrDefault(tbOrderId, Lists.newArrayList()))
                    .id(tbOrderId)
                    .ctime(tbOrder.getCtime())

                    .status(orderStatus)
                    .statusDesc(OrderStatusEnum.getDescByCode(orderStatus))

                    .sellerId(tbOrder.getSellerId())
                    .sellerName(sellerMap.getOrDefault(tbOrder.getSellerId(), StrUtil.EMPTY))

                    .addressId(tbOrder.getAddressId())
                    .addrDesc(tbAddress != null ? tbAddress.getAddrDesc() : StrUtil.EMPTY)
                    .addrUsername(tbAddress != null ? tbAddress.getAddrUsername() : StrUtil.EMPTY)
                    .addrPhone(tbAddress != null ? tbAddress.getAddrPhone() : StrUtil.EMPTY)

                    .delinum(tbOrder.getDeliveryNumber())
                    .delicom(tbOrder.getDelicom())
                    .build();
            res.add(orderInfo);
        }

        BasePageVO<List<OrderInfo4BuyerVO>> basePageVO = new BasePageVO<List<OrderInfo4BuyerVO>>().success();
        basePageVO.setData(res);
        PageVO pageVO = new PageVO(pageSize, pageNum);
        pageVO.setPages(resPage.getPages());
        pageVO.setTotal(resPage.getTotal());
        basePageVO.setPage(pageVO);
        return basePageVO;
    }

    public void cancelOrder(Integer orderId) {
        if (orderId == null) {
            log.error("orderId == null.");
            return;
        }

        TbOrder order = tbOrderService.getById(orderId);
        // 取消订单需要是待付款状态的订单
        Integer status = order.getStatus();
        if (!OrderStatusEnum.PENDING_PAYMENT.getCode().equals(status)) {
            log.error("订单状态不符合预期： order: {}", order);
            return;
        }

        order.setStatus(OrderStatusEnum.CANCELLED.getCode());
        tbOrderService.updateById(order);
    }
}
