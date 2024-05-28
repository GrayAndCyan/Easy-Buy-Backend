package com.mizore.easybuy.annotation;

import com.mizore.easybuy.model.enums.EventTypeEnums;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Audit {

    EventTypeEnums eventType() default EventTypeEnums.UNKNOWN;
}
