/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.05.2010 18:41:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Explains how to get localized value of an attribute
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LocalizedValue {

    /**
     * Explicit definition of a messages pack, e.g. "com.haulmont.cuba.core.entity"
     */
    String messagePack() default "";

    /**
     * Expression in terms of traversing object graph, returning messages pack name stored in an attribute,
     * e.g. "proc.messagesPack"
     */
    String messagePackExpr() default "";
}
