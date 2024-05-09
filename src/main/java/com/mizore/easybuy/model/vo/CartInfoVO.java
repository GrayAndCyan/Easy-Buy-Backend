package com.mizore.easybuy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartInfoVO {

    /**
     * 店铺id
     */
    private Integer sellerId;

    /**
     * 店铺名称
     */
    private String sellerName;

    // 用户购物车中在某家店铺买的所有商品的list
    private List<CartItemVO> content;

}
