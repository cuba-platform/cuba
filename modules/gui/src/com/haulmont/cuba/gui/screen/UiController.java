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
    @AliasFor("id")
    String value() default "";

    @AliasFor("value")
    String id() default "";

    // todo move to separate annotation
    boolean multipleOpen() default false;
}