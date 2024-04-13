package com.mizore.easybuy.model.query;

import lombok.Data;

@Data
public class PayQuery {

    // 订单编号
    private String traceNo;

    // 总金额
    private double totalAmount;

    // 支付名称
    private String subject;

    // ??
    private String alipayTraceNo;
}
