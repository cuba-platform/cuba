/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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