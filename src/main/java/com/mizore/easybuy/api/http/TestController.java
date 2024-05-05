package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.vo.BasePageVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.BanService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@ConfigurationProperties(prefix = "test")
@Setter
public class TestController {

    private int version;

    @GetMapping("initpage")
    public BasePageVO testPageInit() {
        BasePageVO basePageVO = new BasePageVO();
        basePageVO.success();
        return basePageVO;
    }

    @GetMapping("init")
    public BaseVO testInit() {
        return new BaseVO().success().setData("src: 04220227, ver: " + version);
    }

    @Autowired
    private BanService banService;

    @GetMapping("/isBanning")
    public boolean isBanning(@RequestParam Integer userId) {
        return banService.isBanning(userId);
    }

}
