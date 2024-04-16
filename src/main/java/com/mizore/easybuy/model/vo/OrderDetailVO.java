package com.mizore.easybuy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVO {

    // 取商品的第一张描述图 （id最小的）
    private String imgUrl;

    // 商品id
    private Integer itemId;

    // 商品描述
    private String itemDesc;

    // 商品单价
    private BigDecimal unitPrice;

    // 购买个数
    private Integer quantity;

    // 购买该商品的小计总额
    private BigDecimal subtotal;

}
