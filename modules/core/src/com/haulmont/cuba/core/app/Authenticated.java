/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for MBean methods which requires login<br/>
 * Class must be *MBean and subclass of {@link com.haulmont.cuba.core.app.ManagementBean}<br/>
 * Interceptor performs {@link com.haulmont.cuba.core.app.ManagementBean#loginOnce} before method invocation
 *
 * @author artamonov
 * @version $Id$
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Authenticated {
}