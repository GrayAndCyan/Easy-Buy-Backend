package com.mizore.easybuy.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class OrderInfo4SellerVO {

    // 订单号
    private Integer id;

    // 内容
    private List<OrderDetailVO> content;

    // 下单用户id
    private Integer userId;

    // 下单用户名
    private String username;


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
     * 创建时间
     */
    private LocalDateTime ctime;

}
