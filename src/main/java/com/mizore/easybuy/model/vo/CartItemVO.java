package com.mizore.easybuy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemVO {

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 商品名称
     */
    private String title;

    /**
     * 商品图片url
     */
    private String img;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 购物车中该商品数量
     */
    private Integer quantity;

}
