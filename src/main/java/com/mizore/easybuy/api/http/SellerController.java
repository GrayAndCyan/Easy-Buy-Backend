package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.query.SellerUpdateQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author mizore
 * @since 2024-04-06
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    // 修改店铺信息     店名就不让变了吧。。能改的只有地址了目前
    @PostMapping("/update")
    public BaseVO<Object> update(@RequestBody SellerUpdateQuery sellerUpdateQuery) {
        sellerService.update(sellerUpdateQuery);
        return new BaseVO<>().success();
    }

    // 删除店铺 同时取消卖家身份
    @PostMapping("/del/{sellerId}")
    public BaseVO<Object> del(@PathVariable(value = "sellerId") Integer sellerId) throws Exception {
        sellerService.del(sellerId);
        return new BaseVO<>().success();
    }
}
