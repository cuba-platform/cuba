/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The PostConstruct annotation is used on a public no-args method, that needs to be executed by
 * {@link com.haulmont.cuba.core.global.Metadata} framework on {@link com.haulmont.cuba.core.entity.Entity}
 * instance to perform some basic initialization operations.
 *
 * Metadata framework considers inheritance, so you need to override and put the annotation on
 * inherited methods marked with PostConstruct, if you want to change initialization behavior.
 * Methods of ancestors are invoked before methods of descendants.
 *
 * @author Sergey Saiyan
 * @version $Id$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PostConstruct {
}
