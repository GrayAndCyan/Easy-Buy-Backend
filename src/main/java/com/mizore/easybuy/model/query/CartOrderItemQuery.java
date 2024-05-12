package com.mizore.easybuy.model.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartOrderItemQuery {

    // 商品id
    private int itemId;

    // 商品数量
    private int quantity;

    // 商品单价
    private BigDecimal unitPrice;

}
