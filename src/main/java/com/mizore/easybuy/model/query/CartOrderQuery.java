package com.mizore.easybuy.model.query;

import lombok.Data;

import java.util.List;

@Data
public class CartOrderQuery {

    // 店铺id
    private int sellerId;

    // 购物车中该店铺的所有商品CartOrderItemQuery的列表
    private List<CartOrderItemQuery> items;

}
