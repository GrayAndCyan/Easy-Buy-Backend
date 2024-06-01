package com.mizore.easybuy.api.http;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.mizore.easybuy.model.vo.AuditEnumVO;
import com.mizore.easybuy.model.vo.AuditVO;
import com.mizore.easybuy.model.vo.BaseVO;
import com.mizore.easybuy.service.business.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;
    @GetMapping("/search")
    public BaseVO<List<AuditVO>> search(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "objectId", required = false) Integer objectId,
            @RequestParam(value = "objectType", required = false) String objectType,
            @RequestParam(value = "eventType", required = false) String eventType,
            @RequestParam(value = "detailKeywords", required = false) String detailKeywords,
            @RequestParam(value = "actionTime", required = false) Long startTime,
            @RequestParam(value = "actionTime", required = false) Long endTime
    ) {
        return new BaseVO<List<AuditVO>>().success().setData(auditService.search(userId, username,
                objectId, objectType,
                eventType, detailKeywords,
                startTime, endTime));
    }

    @GetMapping("/meta")
    public BaseVO<AuditEnumVO> getEnums() {
        return new BaseVO<AuditEnumVO>().success().setData(AuditEnumVO.getInstance());
    }

}
