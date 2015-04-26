/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author krivenko
 * @version $Id$
 */
@SuppressWarnings("unused")
@ManagedResource(description = "Provides information about web-client requests statistics")
public interface StatisticsCounterMBean {

    Long getWebClientRequestsCount();
}
