package com.mizore.easybuy.service.business;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.mizore.easybuy.config.AliPayConfig;
import com.mizore.easybuy.model.entity.TbOrder;
import com.mizore.easybuy.model.enums.OrderStatusEnum;
import com.mizore.easybuy.model.query.PayQuery;
import com.mizore.easybuy.service.base.impl.TbOrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PayService {

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private TbOrderServiceImpl tbOrderService;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    private static final String FORMAT = "JSON";

    private static final String CHARSET = "UTF-8";


    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    public void createPay(PayQuery payQuery, HttpServletResponse httpResponse) throws IOException {
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", payQuery.getTraceNo());  // 我们自己生成的订单编号
        bizContent.set("total_amount", payQuery.getTotalAmount()); // 订单的总金额
        bizContent.set("subject", payQuery.getSubject());   // 支付的名称
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());

        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @Transactional(rollbackFor = Exception.class)
    public void payCallBack(HttpServletRequest request) throws AlipayApiException {

        if (!request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            // 支付异常
            log.error("支付异常，回调 req： {}", request);
            return;
        }
        log.info("支付回调，req： {}", request);

        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }

        String outTradeNo = params.get("out_trade_no");
//        String gmtPayment = params.get("gmt_payment");
//        String alipayTradeNo = params.get("trade_no");

        String sign = params.get("sign");
        String content = AlipaySignature.getSignCheckContentV1(params);
        boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
        // 支付宝验签
/*        if (!checkSignature) {
            log.error("支付宝验签失败， req: {}", requestParams);
            return;
        }*/
        // 验签通过
        log.info("交易名称: {}，交易状态: {}，支付宝交易凭证号: {}，商户订单号: {}，交易金额: {}，买家在支付宝唯一id: {}，买家付款时间: {}，买家付款金额: {}",
                params.get("subject"), params.get("trade_status"), params.get("trade_no"), params.get("out_trade_no"),
                params.get("total_amount"), params.get("buyer_id"), params.get("gmt_payment"), params.get("buyer_pay_amount"));

        // 查询订单，更改状态===》支付成功
        TbOrder order = tbOrderService.getById(outTradeNo);
        if (order == null) {
            log.error("order is null. order id : {}", outTradeNo);
            return;
        }
        if (!OrderStatusEnum.PENDING_PAYMENT.getCode().equals(order.getStatus())) {
            log.error("order status error, should be PENDING_PAYMENT. but find status: {}", order.getStatus());
            return;
        }
        log.info("订单状态值：OrderStatusEnum.PROCESSING.getCode():{}",OrderStatusEnum.PROCESSING.getCode());
        order.setStatus(OrderStatusEnum.PROCESSING.getCode());
        tbOrderService.updateById(order);

        // 查询对应的子订单，更改状态===》支付成功
        Integer parentOrderId = order.getId();
        LambdaQueryWrapper<TbOrder> query = new LambdaQueryWrapper<TbOrder>()
                .eq(TbOrder::getParentId, parentOrderId);
        List<TbOrder> childOrders = tbOrderService.list(query);
        for(TbOrder childOrder: childOrders) {
            if (!OrderStatusEnum.PENDING_PAYMENT.getCode().equals(childOrder.getStatus())) {
                log.error("childorder status error, should be PENDING_PAYMENT. but find status: {}", childOrder.getStatus());
                return;
            }
            log.info("订单状态值：OrderStatusEnum.PROCESSING.getCode():{}",OrderStatusEnum.PROCESSING.getCode());
            childOrder.setStatus(OrderStatusEnum.PROCESSING.getCode());
            tbOrderService.updateById(childOrder);
        }
    }
}