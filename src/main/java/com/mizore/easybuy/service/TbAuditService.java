package com.mizore.easybuy.service;

import com.mizore.easybuy.model.entity.TbAudit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author mizore
* @description 针对表【tb_audit(数据审计表)】的数据库操作Service
* @createDate 2024-05-25 22:33:48
*/
public interface TbAuditService extends IService<TbAudit> {

    List<TbAudit> search(Integer userId, String username, Integer objectId, String objectType, String eventType, String detailKeywords, Long startTime, Long endTime);
}
