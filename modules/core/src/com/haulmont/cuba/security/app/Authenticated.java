/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.app;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for bean methods which require authentication, i.e. presence of a valid user session.
 *
 * @author artamonov
 * @version $Id$
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Authenticated {
}