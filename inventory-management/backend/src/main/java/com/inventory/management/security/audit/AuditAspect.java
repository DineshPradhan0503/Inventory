package com.inventory.management.security.audit;

import com.inventory.management.services.AuditService;
import com.inventory.management.security.services.UserDetailsImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditService auditService;

    @AfterReturning("@annotation(com.inventory.management.security.audit.Audited)")
    public void afterAuditedMethod(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = "anonymous";
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl user) {
            userId = user.getId();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audited audited = method.getAnnotation(Audited.class);

        String resourceId = null;
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] != null) {
            resourceId = args[0].toString();
        }

        auditService.record(userId, audited.action(), audited.resource(), resourceId, method.getName());
    }
}

