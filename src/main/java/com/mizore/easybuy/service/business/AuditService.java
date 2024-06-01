package com.mizore.easybuy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.mizore.easybuy.model.entity.TbAudit;
import com.mizore.easybuy.model.vo.AuditVO;
import com.mizore.easybuy.service.TbAuditService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private TbAuditService tbAuditService;

    public List<AuditVO> search(Integer userId, String username,
                                Integer objectId, String objectType,
                                String eventType, String detailKeywords,
                                Long startTime, Long endTime) {
        List<TbAudit> tbAudits = tbAuditService.search(userId, username,
                objectId, objectType,
                eventType, detailKeywords,
                startTime, endTime);

        List<AuditVO> auditVOS = Lists.newArrayList();
        if (CollectionUtil.isEmpty(tbAudits)) {
            return auditVOS;
        }

        tbAudits.forEach(tbAudit -> {
            AuditVO auditVO = new AuditVO();
            BeanUtils.copyProperties(tbAudit, auditVO);
            auditVOS.add(auditVO);
        });

        return auditVOS;
    }
}
