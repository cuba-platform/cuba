/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackEditScreenHistory {

    boolean value() default true;
}
