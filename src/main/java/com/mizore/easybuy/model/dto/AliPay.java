package com.mizore.easybuy.model.dto;

import lombok.Data;

@Data
public class AliPay {

    // 订单编号
    private String traceNo;

    // 总金额
    private double totalAmount;

    // 支付名称
    private String subject;

    //
    private String alipayTraceNo;
}
