package com.mizore.easybuy.api.http;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;
import com.mizore.easybuy.config.AliPayConfig;
import com.mizore.easybuy.model.query.PayQuery;
import com.mizore.easybuy.service.business.PayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping("/create") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(PayQuery payQuery, HttpServletResponse httpResponse) {
        try {
            payService.createPay(payQuery, httpResponse);
        } catch (Exception e) {
            log.error("支付单创建失败 ",e);
        }
    }

    /**
     * 支付成功回调
     */
    @PostMapping("/callback")
    public void payCallBack(HttpServletRequest request){
        try {
            payService.payCallBack(request);
        } catch (AlipayApiException e) {
            log.error("支付回调异常");
        }
    }



}
