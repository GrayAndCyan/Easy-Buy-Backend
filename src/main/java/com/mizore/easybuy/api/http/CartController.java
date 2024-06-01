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
        log.info("进入添加至购物车接口");
        log.info("添加至购物车--itemid:{}, quantity:{}",cartAddQuery.getItemId(),cartAddQuery.getQuantity());
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
        log.info("卖家id：{}",cartOrderQuery.get(0).getSellerId());
        return cartService.placeOrder(cartOrderQuery, addressId);
    }

    /**
     * 从购物车中移除某商品
     * @param itemId 商品id
     * @return
     */
    @PostMapping("/remove/{itemId}")
    public BaseVO<Object> removeItem(
            @PathVariable(value = "itemId") Integer itemId
    ) {
        return cartService.removeItem(itemId);
    }

    /**
     * 更改商品数量--加一
     */
    @PostMapping("/addone")
    public BaseVO<Object> addOne(
            @RequestBody CartAddQuery cartAddQuery
    ) {
        return cartService.addOne(cartAddQuery);
    }

    /**
     * 更改商品数量--减一
     */
    @PostMapping("/minusone")
    public BaseVO<Object> minusOne(
            @RequestBody CartAddQuery cartAddQuery
    ) {
        return cartService.minusOne(cartAddQuery);
    }

}
