package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("initpage")
    public BasePageVO testPageInit() {
        BasePageVO basePageVO = new BasePageVO();
        basePageVO.success();
        return basePageVO;
    }

    @GetMapping("init")
    public BaseVO testInit() {
        return new BaseVO().success();
    }

}
