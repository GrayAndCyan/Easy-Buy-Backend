package com.mizore.easybuy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo4BuyerVO {

    // 订单号
    private Integer id;

    // 内容
    private List<OrderDetailVO> content;

    // 店铺id
    private Integer sellerId;

    // 店铺名称
    private String sellerName;


    // 状态值
    private Integer status;

    // 状态描述
    private String statusDesc;

    /**
     * 地址id
     */
    private Integer addressId;

    /**
     * 地址描述
     */
    private String addrDesc;

    /**
     * 地址对应的姓名
     */
    private String addrUsername;

    /**
     * 地址预留手机号
     */
    private String addrPhone;

    /**
     * 订单总额
     */
    private BigDecimal totalAmount;

    /**
     * 订单号
     */
    private String delinum;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

}
