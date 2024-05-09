package com.mizore.easybuy.api.http;

import com.alipay.api.domain.EduOneCardDepositCardQueryResult;
import com.mizore.easybuy.model.query.CartAddQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
