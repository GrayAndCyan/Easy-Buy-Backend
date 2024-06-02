package com.mizore.easybuy.aspect;

import com.alibaba.fastjson.JSON;
import com.mizore.easybuy.annotation.Audit;
import com.mizore.easybuy.model.dto.UserDTO;
import com.mizore.easybuy.model.entity.TbAudit;
import com.mizore.easybuy.utils.HttpUtils;
import com.mizore.easybuy.utils.UserHolder;
import com.mizore.easybuy.utils.audit.AuditHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class AuditAspect {

    // 切入点在加了@Audit注解的类与方法
    @Pointcut("@within(com.mizore.easybuy.annotation.Audit) || @annotation(com.mizore.easybuy.annotation.Audit)")
    public void auditPointcut() {}


    @Before("auditPointcut()")
    public void beforeAudit(JoinPoint joinPoint) {
        // 避免线程复用造成的错误
        AuditHolder.remove();

        TbAudit audit = new TbAudit();
        AuditHolder.save(audit);
        // 填充audit用户信息
        HttpServletRequest request = HttpUtils.getRequest();
        UserDTO userDTO = UserHolder.get();
        if (userDTO == null) {
            log.warn("userDTO is null. requestURI: {}", request.getRequestURI());
        } else {
            audit.setUserId(userDTO.getId())
                    .setUsername(userDTO.getUsername());
        }

        // 填充事件类型
        Audit auditAnno = AnnotationUtils.findAnnotation(((MethodSignature)joinPoint.getSignature()).getMethod(), Audit.class);
        if (auditAnno != null) {
            String eventTypeDesc = auditAnno.eventType().getDesc();
            audit.setEventType(eventTypeDesc);
        }

        // 填充其他信息
        audit.setRequestUrl(request.getRequestURI())
                .setRequestParam(JSON.toJSONString(request.getParameterMap()))
                .setActionTime(LocalDateTime.now());
    }

    @After("auditPointcut()")
    public void afterAudit(JoinPoint joinPoint) {
        // 在方法执行后做资源清理
        AuditHolder.remove();
    }
}
