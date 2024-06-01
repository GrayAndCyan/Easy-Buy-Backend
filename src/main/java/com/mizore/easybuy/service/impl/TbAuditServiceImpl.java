package com.mizore.easybuy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.easybuy.model.entity.TbAudit;
import com.mizore.easybuy.service.TbAuditService;
import com.mizore.easybuy.mapper.TbAuditMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
* @author mizore
* @description 针对表【tb_audit(数据审计表)】的数据库操作Service实现
* @createDate 2024-05-25 22:33:48
*/
@Service
public class TbAuditServiceImpl extends ServiceImpl<TbAuditMapper, TbAudit>
    implements TbAuditService{

    @Override
    public List<TbAudit> search(Integer userId, String username,
                       Integer objectId, String objectType,
                       String eventType, String detailKeywords,
                       Long startTime, Long endTime) {

        LambdaQueryWrapper<TbAudit> query = new LambdaQueryWrapper<>();

        if (startTime != null && endTime != null) {
            LocalDateTime startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
            LocalDateTime endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
            query.between(TbAudit::getActionTime, startDateTime, endDateTime);
        } else if (startTime != null) {
            LocalDateTime startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
            query.ge(TbAudit::getActionTime, startDateTime);
        } else if (endTime != null) {
            LocalDateTime endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
            query.le(TbAudit::getActionTime, endDateTime);
        }

        if (userId != null) {
            query.eq(TbAudit::getUserId, userId);
        }
        if (StrUtil.isNotBlank(username)) {
            query.eq(TbAudit::getUsername, username);
        }
        if (objectId != null) {
            query.eq(TbAudit::getObjectId, objectId);
        }
        if (StrUtil.isNotBlank(objectType)) {
            query.eq(TbAudit::getObjectType, objectType);
        }
        if (StrUtil.isNotBlank(eventType)) {
            query.eq(TbAudit::getEventType, eventType);
        }
        if (StrUtil.isNotBlank(detailKeywords)) {
            query.like(TbAudit::getDetail, detailKeywords);
        }

        return list(query);
    }
}




