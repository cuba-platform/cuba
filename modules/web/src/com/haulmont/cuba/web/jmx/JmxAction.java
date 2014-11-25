/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;

import javax.management.MBeanServerConnection;

/**
 * @author artamonov
 * @version $Id$
 */
public interface JmxAction<T> {

    T perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception;
}