/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.01.2009 13:05:45
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity.annotation;

import com.haulmont.cuba.core.global.DeletePolicy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Marks a link to other entity for specific soft deletion behaviour.<br>
 * <b>Taken into account by persistence when "linked" entity (which is referenced by the marked field)
 * is deleted.</b><br>
 * See also {@link DeletePolicy}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OnDeleteInverse
{
    DeletePolicy value();
}
