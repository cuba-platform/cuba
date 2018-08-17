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
public @interface UiDescriptor {
    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";
}