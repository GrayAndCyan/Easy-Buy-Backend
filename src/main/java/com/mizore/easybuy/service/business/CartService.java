package com.mizore.easybuy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.*;
import com.mizore.easybuy.model.enums.ItemStatusEnum;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.query.CartAddQuery;
import com.mizore.easybuy.model.query.CartOrderItemQuery;
import com.mizore.easybuy.model.query.CartOrderQuery;
import com.mizore.easybuy.model.vo.*;
import com.mizore.easybuy.service.base.*;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {

    @Autowired
    private ITbCartService tbCartService;

    @Autowired
    private ITbItemService tbItemService;

    @Autowired
    private ITbItemImageService tbItemImageService;

    @Autowired
    private ITbSellerService tbSellerService;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private OrderService orderService;

    // 商品加入购物车
    public BaseVO<Object> addToCart(CartAddQuery cartAddQuery) {
        BaseVO<Object> baseVO = new BaseVO<>();
        boolean res;
        // 当前登录用户
        UserDTO user = UserHolder.get();
        // 获取用户id
        Integer userId = user.getId();
        // 获取商品id
        Integer itemId = cartAddQuery.getItemId();
        // 获取当前用户购物车中是否已经有该商品记录
        TbCart tbCart = tbCartService.itemExist(userId, itemId);
        // 若存在，则更新quantity
        if(tbCart != null) {
            Integer quantity = cartAddQuery.getQuantity() + tbCart.getQuantity();
            // 在数据库更新购物车中商品数量
            tbCart.setQuantity(quantity);
            res = tbCartService.updateById(tbCart);
        } else {    // 不存在则插入一条购物车信息
            // 创建一条购物车信息
            TbCart newCart = TbCart.builder()
                    .userId(userId)
                    .itemId(itemId)
                    .quantity(cartAddQuery.getQuantity())
                    .build();
            // 将购物车信息插入数据库
            res = tbCartService.save(newCart);
        }

        return res ? baseVO.success()
                : baseVO.setCode(ReturnEnum.FAILURE.getCode()).setMessage("operation failed");
    }

    public BasePageVO<List<CartInfoVO>> displayAll(Integer pageSize, Integer pageNum) {
        // 设置默认页面信息
        if (pageSize == null || pageNum == null) {
            pageSize = 1;
            pageNum = 10;
        }
        Page<Object> resPage = PageHelper.startPage(1, 10);
        // 获取当前登录用户
        UserDTO user = UserHolder.get();
        // 当前登录用户id
        Integer userId = user.getId();
        // 获取当前用户购物车中所有条目
        List<TbCart> tbCart = tbCartService.search(userId);
        // 获取涉及到的所有itemId
        List<Integer> itemIds = tbCart.stream()
                .map(TbCart::getItemId)
                .toList();
        // 从商品表中批查得到商品信息
        List<TbItem> tbItems = tbItemService.listByIds(itemIds);
        // itemId -> TbItem Entity
        Map<Integer, TbItem> itemMap;
        if (CollectionUtil.isEmpty(itemIds)) {
            itemMap = Maps.newHashMap();
            log.error("empty item ids");
        } else {
            // itemId -> TbItem Entity
            itemMap = tbItems.stream()
                    .collect(Collectors.toMap(TbItem::getId, x->x));
        }
        // 对每条购物车记录(Cart对象)构造CartItemVO List<TbCart> -> List<CartItemVO>
        List<CartItemVO> cartItemVOs = tbCart.stream().map(
                x -> {
                    Integer itemId = x.getItemId(); // 当前TbCart对象的itemId
                    TbItem tbItem = itemMap.get(itemId);
                    return CartItemVO.builder()
                            .itemId(itemId)
                            .title(tbItem.getTitle())
                            .img(tbItemImageService.getFirstImgUrl(itemId))
                            .description(tbItem.getDescription())
                            .price(tbItem.getPrice())
                            .quantity(x.getQuantity())
                            .status(tbItem.getStatus())
                            .statusDesc(ItemStatusEnum.getDescByCode(tbItem.getStatus()))
                            .build();
                }
        ).toList();
        // 根据店铺分组
        // 获取所有涉及的店铺id
        Set<Integer> sellerIds = tbItems.stream()
                .map(TbItem::getSellerId)
                .collect(Collectors.toSet());
        // sellerId -> sellerName
        Map<Integer, String> sellerMap;
        if (CollectionUtil.isEmpty(sellerIds)) {
            sellerMap = Maps.newHashMap();
            log.error("empty seller ids");
        } else {
            List<TbSeller> sellers = tbSellerService.listByIds(sellerIds);
            sellerMap = sellers.stream()
                    .collect(Collectors.toMap(TbSeller::getId, TbSeller::getName));
        }
        // key-店铺id value-所有具有对应sellerId的CartItemVO对象列表
        Map<Integer, List<CartItemVO>> sellerItemsMap = cartItemVOs.stream()
                .collect(Collectors.groupingBy(x -> itemMap.get(x.getItemId()).getSellerId()));
        // convert to cartInfoVO
        List<CartInfoVO> cartInfoVOs = sellerItemsMap.entrySet().stream().map(
                x -> {
                    Integer sellerId = x.getKey();
                    return CartInfoVO.builder()
                            .sellerId(sellerId)
                            .sellerName(sellerMap.get(sellerId))

                            .content(x.getValue())
                            .build();
                }
        ).toList();

        BasePageVO<List<CartInfoVO>> basePageVO = new BasePageVO<List<CartInfoVO>>().success();
        basePageVO.setData(cartInfoVOs);
        PageVO pageVO = new PageVO(pageSize, pageNum);
        pageVO.setPages(resPage.getPages());
        pageVO.setTotal(resPage.getTotal());
        basePageVO.setPage(pageVO);
        return basePageVO;
    }

    // r2下单功能
    @Transactional
    public BaseVO<PlaceOrderVO> placeOrder(List<CartOrderQuery> cartOrderQuery,
                                           Integer addressId) {
        // 获取当前登录用户
        UserDTO user = UserHolder.get();
        // 获取用户id
        Integer userId = user.getId();
        // 总订单(后面所有订单的父订单)
        TbOrder blanketOrder = new TbOrder();
        // 设置下单用户id
        blanketOrder.setUserId(userId);
        // 设置订单状态为“待付款”
        blanketOrder.setStatus(OrderStatusEnum.PENDING_PAYMENT.getCode());
        // 设置地址id
        blanketOrder.setAddressId(addressId);
        // 将父订单插入数据库
        tbOrderService.save(blanketOrder);
        // 获得父订单id
        Integer parentId = blanketOrder.getId();

        BigDecimal sum = BigDecimal.valueOf(0); // 父订单总额
        // 遍历每个店铺，依次下单，计算父订单总额
        for(CartOrderQuery shop: cartOrderQuery) {
            // 获得店铺id
            Integer sellerId = shop.getSellerId();
            // 获得要在该店铺下单的所有item
            List<CartOrderItemQuery> items = shop.getItems();
            // List<CartOrderItemQuery> -> List<TbOrderDetail>
            List<TbOrderDetail> tbOrderDetails = items.stream().map(
                    x -> {
                        TbOrderDetail tbOrderDetail = new TbOrderDetail();
                        tbOrderDetail.setItemId(x.getItemId());
                        tbOrderDetail.setQuantity(x.getQuantity());
                        tbOrderDetail.setUnitPrice(x.getUnitPrice());
                        return tbOrderDetail;
                    }
            ).toList();
            // 下单
            TbOrder tbOrder = orderService.doPlaceOrder(tbOrderDetails, addressId, sellerId);
            // 设置父订单id，更新数据库表
            tbOrder.setParentId(parentId);
            tbOrderService.updateById(tbOrder);
            // 在购物车中找该商品，若该商品是在购物车中的商品，则下单后删除对应记录
            // 获取涉及到的所有itemId
            List<Integer> itemIds = items.stream()
                    .map(CartOrderItemQuery::getItemId)
                    .toList();
            // 从购物车中删除
            removeAfterOrder(userId, itemIds);
            // 累加父订单总额
            sum = sum.add(tbOrder.getTotalAmount());
        }
        // 更新父订单总额
        blanketOrder.setTotalAmount(sum);
        tbOrderService.updateById(blanketOrder);

        TbOrder parentOrder = tbOrderService.getById(blanketOrder);

        PlaceOrderVO orderVO = PlaceOrderVO.builder()
                .orderId(parentOrder.getId())
                .totalAmount(parentOrder.getTotalAmount())
                .userId(userId)
                .userName(user.getUsername())
                .ctime(parentOrder.getCtime())
                .build();

        BaseVO<PlaceOrderVO> baseVO = new BaseVO<PlaceOrderVO>().success();
        baseVO.setData(orderVO);
        return baseVO;
    }

    // 下单后从购物车中删除记录
    private void removeAfterOrder(Integer userId, List<Integer> itemIds) {
        for(Integer itemId: itemIds) {
            TbCart tbCart = tbCartService.itemExist(userId, itemId);
            // 购物车中存在相应的记录
            if(tbCart != null) {
                tbCartService.removeById(tbCart);
            }
        }
    }

    // 从购物车中移除商品
    public BaseVO<Object> removeItem(Integer itemId) {
        UserDTO user = UserHolder.get();
        Integer userId = user.getId();      // 用户id
        // 移除
        QueryWrapper<TbCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("item_id", itemId);
        boolean res = tbCartService.remove(queryWrapper);
        BaseVO<Object> baseVO = new BaseVO<>();
        return res ? baseVO.success()
                : baseVO.setCode(ReturnEnum.FAILURE.getCode()).setMessage("operation failed");
    }

    // 更改商品数量--加一
    public BaseVO<Object> addOne(CartAddQuery cartAddQuery) {
        BaseVO<Object> baseVO = new BaseVO<>().success();
        // 当前登录用户
        UserDTO user = UserHolder.get();
        Integer userId = user.getId();
        // 商品id
        Integer itemId = cartAddQuery.getItemId();
        // 购物车中该商品数量
        Integer quantity = cartAddQuery.getQuantity();
        TbCart tbCart = tbCartService.itemExist(userId, itemId);
        if(tbCart != null) {
            // 数量+1 存数据库
            tbCart.setQuantity(quantity + 1);
            tbCartService.updateById(tbCart);
            baseVO.setData(tbCart.getQuantity());
        }
        return baseVO;
    }

    // 更改商品数量--减一
    public BaseVO<Object> minusOne(CartAddQuery cartAddQuery) {
        BaseVO<Object> baseVO = new BaseVO<>().success();
        // 当前登录用户
        UserDTO user = UserHolder.get();
        Integer userId = user.getId();
        // 商品id
        Integer itemId = cartAddQuery.getItemId();
        // 购物车中该商品数量
        Integer quantity = cartAddQuery.getQuantity();
        TbCart tbCart = tbCartService.itemExist(userId, itemId);
        if(tbCart != null) {
            // 购物车中物品数量最少为1，若等于1则点减号按钮不改变数量
            if(quantity > 1) {
                // 数量-1 存数据库
                tbCart.setQuantity(quantity - 1);
                tbCartService.updateById(tbCart);
            }
            baseVO.setData(tbCart.getQuantity());
        }
        return baseVO;
    }
}
