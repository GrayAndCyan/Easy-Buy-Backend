package com.mizore.easybuy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo4SellerVO {

    // 商品id
    private Integer itemId;

    // 取商品的第一张描述图 （id最小的）
    private String imgUrl;

    // 商品标题
    private String title;

    // 商品描述
    private String description;

    // 定价
    private BigDecimal price;

    // 库存
    private Integer stock;

    // 状态
    private Integer status;

    // 状态描述
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

}
