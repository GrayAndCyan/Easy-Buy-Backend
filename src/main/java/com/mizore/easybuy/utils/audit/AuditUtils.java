package com.mizore.easybuy.utils.audit;

import com.mizore.easybuy.model.entity.TbAudit;
import com.mizore.easybuy.service.TbAuditService;
import com.mizore.easybuy.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AuditUtils {

    private static TbAuditService tbAuditService;

    public static CompletableFuture<Void> doAuditAsync(String detail, int objectId, String objectType) {
        return doAuditAsync(detail, objectId, objectType, null);
    }

    public static CompletableFuture<Void> doAuditAsync(String detail, int objectId,
                                                       String objectType, String eventType) {
        var ref = new Object() {
            TbAudit audit = AuditHolder.get();
        };

        return CompletableFuture.supplyAsync(() -> {
            if (ref.audit == null) {
                log.warn("AuditUtils#AuditUtils: Audit object is null");
                ref.audit = new TbAudit();
            }
            if (eventType != null) {
                ref.audit.setEventType(eventType);
            }
            ref.audit.setDetail(detail)
                    .setObjectId(objectId)
                    .setObjectType(objectType);
            return ref.audit; // 将Audit对象返回供下游处理
        }).thenAcceptAsync(auditResult -> {
            // 在异步任务完成后，使用Audit对象进行保存等操作
            getAuditService().save(auditResult);
        });
    }

    // double-check 懒初始化auditService
    private static TbAuditService getAuditService() {
        if (tbAuditService != null) {
            return tbAuditService;
        }
        synchronized (AuditUtils.class) {
            return Objects.requireNonNullElseGet(tbAuditService,
                    () -> tbAuditService = BeanUtils.getBean(TbAuditService.class));
        }
    }

}
