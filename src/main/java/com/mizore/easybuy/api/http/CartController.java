package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.query.CartAddQuery;
import com.mizore.easybuy.model.query.CartOrderQuery;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.CartInfoVO;
import com.mizore.easybuy.model.vo.PlaceOrderVO;
import com.mizore.easybuy.service.business.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 用户添加商品至购物车
     */
    @PostMapping("/add")
    public BaseVO<Object> addToCart(
            @RequestBody CartAddQuery cartAddQuery
            ) {
        return cartService.addToCart(cartAddQuery);
    }

    /**
     * 用户查看自己购物车中所有商品
     * @param pageSize 页大小
     * @param pageNum 页码
     */
    @GetMapping("/display/all")
    public BasePageVO<List<CartInfoVO>> displayAll(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        return cartService.displayAll(pageSize, pageNum);
    }

    /**
     * 购物车下单
     * @param cartOrderQuery 以店铺聚合，list中元素为某店铺中要下单的所有商品
     * @param addressId 收货地址id
     * @return
     * @throws RuntimeException
     */
    @PostMapping("/order")
    public BaseVO<PlaceOrderVO> placeOrder(
            @RequestBody List<CartOrderQuery> cartOrderQuery,
            @RequestParam(value = "addressId") Integer addressId
    ) {
        return cartService.placeOrder(cartOrderQuery, addressId);
    }

}
