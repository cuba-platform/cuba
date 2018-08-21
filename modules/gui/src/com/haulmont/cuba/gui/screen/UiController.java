package com.haulmont.cuba.gui.screen;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.lang.annotation.Target;

/**
 * JavaDoc
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UiController {
    String ID_ATTRIBUTE = "id";
    String VALUE_ATTRIBUTE = "value";

    @AliasFor(ID_ATTRIBUTE)
    String value() default "";

    @AliasFor(VALUE_ATTRIBUTE)
    String id() default "";

    // todo move to separate annotation
    boolean multipleOpen() default false;
}