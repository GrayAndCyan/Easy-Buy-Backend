package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.query.CartAddQuery;
import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.model.vo.CartInfoVO;
import com.mizore.easybuy.service.business.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
