/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.annotation;

import com.haulmont.cuba.core.entity.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Counterpart of {@link Extends}. Normally it is not used as type annotation, but its name is used as a key of
 * meta-annotation for an entity that is extended by another one.
 *
 * @author krivopustov
 * @version $Id$
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendedBy {

    Class<? extends Entity> value();
}
