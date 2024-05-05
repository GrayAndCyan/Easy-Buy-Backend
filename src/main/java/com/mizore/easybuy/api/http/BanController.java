package com.mizore.easybuy.api.http;

import com.mizore.easybuy.model.query.BanQuery;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ban")
public class BanController {

    @Autowired
    private BanService banService;

    @PostMapping
    public BaseVO<Object> banUser(@RequestBody BanQuery banQuery) {
        banService.ban(banQuery.getUserId(), banQuery.getDuration(), banQuery.getReason());
        return new BaseVO<>().success();
    }

}
