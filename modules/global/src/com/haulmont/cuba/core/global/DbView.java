package com.haulmont.cuba.core.global;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates whether a JPA entity is a database view.
 * Development tools do not generate database initialization and update scripts for such entities.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface DbView {
}
