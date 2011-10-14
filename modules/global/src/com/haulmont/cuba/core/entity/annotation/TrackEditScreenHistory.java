/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates whether to track an edit screen opening for this entity. If no such annotation present for an entity, or
 * value() is false, no tracking performed.
 *
 * <p>Can be overridden in <code>*-metadata.xml</code> file.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackEditScreenHistory {

    boolean value() default true;
}
