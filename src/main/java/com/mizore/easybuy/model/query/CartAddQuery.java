package com.mizore.easybuy.model.query;

import lombok.Data;

@Data
public class CartAddQuery {

    // 商品id
    private int itemId;

    // 商品数量
    private int quantity;
}
