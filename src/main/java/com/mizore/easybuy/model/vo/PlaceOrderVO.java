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
public class PlaceOrderVO {

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单总额
     */
    private BigDecimal totalAmount;

    /**
     * 下单用户id
     */
    private Integer userId;

    /**
     * 下单用户名
     */
    private String userName;

    /**
     * 订单创建时间
     */
    private LocalDateTime ctime;
}
