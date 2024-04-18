package com.mizore.easybuy.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mizore.easybuy.mapper.TbSellerMapper;
import com.mizore.easybuy.mapper.TbUserMapper;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.*;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.enums.RoleEnum;
import com.mizore.easybuy.model.vo.*;
import com.mizore.easybuy.service.base.*;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@Service
@Slf4j
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

    @Autowired
    private TbSellerMapper tbSellerMapper;

    @Override
    public TbUser getByUser(int userId) {
        LambdaQueryWrapper<TbUser> query = new LambdaQueryWrapper<TbUser>()
                .eq(TbUser::getId, userId);
        List<TbUser> res = list(query);
        if (res != null && res.size() > 1) {
            log.error("存在脏数据 buyer: {}", Arrays.toString(res.toArray()));
        }
        return CollectionUtil.isEmpty(res) ? null : res.get(0);
    }

    /**
     * 用户开店
     */
    @Transactional
    public BaseVO openStore(String name, String address) {
        BaseVO<String> baseVO = new BaseVO<>();
        // 新增店铺（卖家）
        TbSeller tbSeller = new TbSeller();
        // 获取当前登录用户
        UserDTO userDTO = UserHolder.get();
        // 判断当前用户角色是否为普通买家，若是，则能够开店，赋予其卖家角色
        if(RoleEnum.BUYER.getCode().equals(userDTO.getRole())){
            // 设置卖家对应的用户账号id
            tbSeller.setUserId(userDTO.getId());
            // 设置店铺名称
            tbSeller.setName(name);
            // 设置店铺地址
            tbSeller.setAddress(address);
            // 将新增店铺信息插入数据库
            tbSellerMapper.insert(tbSeller);
            // 赋予用户卖家角色，更新数据库中用户信息
            update().set("role", RoleEnum.SELLER.getCode())
                    .eq("id",userDTO.getId())
                    .eq("role", RoleEnum.BUYER.getCode())
                    .update();
            return baseVO.success();
        } else {
            baseVO.setCode(ReturnEnum.FAILURE.getCode());
            baseVO.setMessage("Open store failed, role error!");
            return baseVO;
        }
    }

    @Autowired
    private ITbSellerService tbSellerService;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbAddressService tbAddressService;

    @Autowired
    private ITbOrderDetailService tbOrderDetailService;

    @Autowired
    private ITbItemImageService tbItemImageService;

    @Autowired
    private ITbItemService tbItemService;
    //供用户查看自己的订单
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

}
