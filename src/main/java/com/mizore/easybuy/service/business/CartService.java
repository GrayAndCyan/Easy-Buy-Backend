package com.mizore.easybuy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbCart;
import com.mizore.easybuy.model.entity.TbItem;
import com.mizore.easybuy.model.entity.TbSeller;
import com.mizore.easybuy.model.enums.ReturnEnum;
import com.mizore.easybuy.model.query.CartAddQuery;
import com.mizore.easybuy.model.vo.*;
import com.mizore.easybuy.service.base.ITbCartService;
import com.mizore.easybuy.service.base.ITbItemImageService;
import com.mizore.easybuy.service.base.ITbItemService;
import com.mizore.easybuy.service.base.ITbSellerService;
import com.mizore.easybuy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
