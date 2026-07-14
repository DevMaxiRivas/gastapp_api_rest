package com.app.aspect.database.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to trigger the 'softDeleteFilter' on Hibernate Session.
 * Can be applied to classes (all methods) or specific methods.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplySoftDelete {
}