package com.app.aspect.database.filter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Aspect
@Component
public class SoftDeleteAspect {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String FILTER_NAME = "softDeleteFilter";

    /**
     * Surrounds the execution of methods annotated with @ApplySoftDelete.
     *
     * @param joinPoint The point of execution (method)
     * @return The result of the method execution
     * @throws Throwable if the underlying method throws an exception
     */
    @Around("@annotation(com.app.aspect.database.filter.ApplySoftDelete) || @within(com.app.aspect.database.filter.ApplySoftDelete)")
    public Object manageSoftDeleteFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);

        try {
            // 1. Enable the filter before method execution
            session.enableFilter(FILTER_NAME);
            return joinPoint.proceed();
        } finally {
            // 2. Ensure the filter is disabled regardless of outcome (success or error)
            // This prevents "pollution" of the Hibernate session for subsequent calls.
            session.disableFilter(FILTER_NAME);
        }
    }
}